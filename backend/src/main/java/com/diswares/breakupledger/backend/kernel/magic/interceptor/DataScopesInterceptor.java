package com.diswares.breakupledger.backend.kernel.magic.interceptor;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.diswares.breakupledger.backend.kernel.magic.annotions.MybatisPlusMapperScopes;
import com.diswares.breakupledger.backend.kernel.magic.annotions.Scoped;
import com.diswares.breakupledger.backend.kernel.magic.design.ScopeExecutor;
import com.diswares.breakupledger.backend.kernel.magic.design.ScopedContext;
import com.diswares.breakupledger.backend.kernel.magic.design.ScopedHandler;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.ApplicationContext;
import weapon.EntityWeapons;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.*;

/**
 * 拦截查询 SQL
 *
 * @author 4everlynn
 */
@Data
@Accessors(fluent = true)
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopesInterceptor implements Interceptor {

    /**
     * 当前上下文
     */
    private ScopedContext scopes;

    /**
     * mybatis plus 分页 auto count 语句
     */
    private static final String MP_PAGE = "_mpCount";

    /**
     * Spring 容器上下文
     */
    private ApplicationContext context;

    private final static Set<String> BASE_MAPPER_METHODS = new HashSet<>();

    static {
        Arrays.stream(BaseMapper.class.getDeclaredMethods())
                .forEach((it) -> BASE_MAPPER_METHODS.add(it.getName()));
    }


    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        // 上下文为空，原样返回
        if (null == scopes) {
            return invocation.proceed();
        }

        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        final String id = mappedStatement.getId();

        // mapper 类
        String mapper = id.substring(0, id.lastIndexOf("."));

        // 调用的 mapper 方法
        String method = id.substring(id.lastIndexOf(".") + 1);

        // 遇到分页插件的前置 count，去除后缀获得原始方法
        if (method.endsWith(MP_PAGE)) {
            method = method.replaceAll(MP_PAGE, "");
        }

        // 仅拦截 BaseMapper 中的方法
        if (!BASE_MAPPER_METHODS.contains(method)) {
            log.warn("{}: Method {} not found, skip it anyway.", MybatisPlusMapperScopes.class.getName(), method);
            return invocation.proceed();
        }

        final Class<?> mapperClass = getMybatisMapperClass(mapper);

        // 处理数据权限
        return this.resolveScopes(mapperClass, invocation, metaObject, statementHandler, mappedStatement);
    }

    private Object resolveScopes(Class<?> mapperClass, Invocation invocation,
                                 MetaObject metaObject, StatementHandler statementHandler, MappedStatement mappedStatement)
            throws InvocationTargetException, IllegalAccessException {

        if (null != mapperClass) {
            if (BaseMapper.class.isAssignableFrom(mapperClass)) {
                // noinspection unchecked
                Class<BaseMapper<?>> mybatis = (Class<BaseMapper<?>>) mapperClass;

                final MybatisPlusMapperScopes mybatisPlusMapperScopes = mybatis.getDeclaredAnnotation(MybatisPlusMapperScopes.class);

                // 仅解析开启注解的 mapper
                if (null == mybatisPlusMapperScopes) {
                    return invocation.proceed();
                }

                final Type[] genericInterfaces = mybatis.getGenericInterfaces();

                // 领域类型
                Class<? extends AncestorDomain> domainType = null;


                for (Type genericInterface : genericInterfaces) {
                    domainType = getAncestorDomain(genericInterface);
                    // 解析出了领域类型，直接退出
                    if (null != domainType) {
                        break;
                    }
                }

                if (null != domainType) {
                    // 继续分析实体类型
                    return this.analysis(domainType, invocation, metaObject, statementHandler, mappedStatement);
                }

            }
        }


        return invocation.proceed();
    }


    @SneakyThrows
    private Object analysis(Class<? extends AncestorDomain> domainType,
                            Invocation invocation, MetaObject metaObject,
                            StatementHandler statementHandler, MappedStatement mappedStatement) {

        final Field[] fields = EntityWeapons.getAllDeclaredFields(domainType);

        final TableName tableName = domainType.getDeclaredAnnotation(TableName.class);

        String table = domainType.getSimpleName();

        if (null != tableName) {
            table = tableName.schema().concat(".").concat(tableName.value());
        }

        List<ScopeExecutor> executors = new ArrayList<>();

        for (Field field : fields) {

            final Scoped scoped = field.getDeclaredAnnotation(Scoped.class);

            // 字段被标记为权限隔离字段
            if (null != scoped) {
                String key = this.getScopedKey(scoped);

                if (Strings.isNotEmpty(key)) {
                    final Map<String, ScopedHandler<?>> handlers = scopes.handlers();
                    if (handlers.containsKey(key)) {

                        // 从上下文对象中获取处理器
                        //noinspection unchecked
                        final ScopedHandler<Object> handler = (ScopedHandler<Object>) handlers.get(key);

                        // 是否跳过本次条件拼接
                        if (handler.skip()) {
                            continue;
                        }
                        final Object currentScopes = handler.scopes();
                        final String sql = handler.builder().sql(currentScopes);

                        executors.add(
                                new ScopeExecutor()
                                        .table(table)
                                        .field(field)
                                        .sqlFragment(sql)
                        );


                    }


                }
            }
        }

        if (executors.size() > 0) {
            String sql = this.parseOriginalSql(statementHandler);
            this.refactoringSql(sql, metaObject, executors, mappedStatement);
        }


        return invocation.proceed();
    }

    /**
     * 重构 SQL
     *
     * @param sql             原始 SQL
     * @param metaObject      Mybatis metaObject
     * @param executors       执行器
     * @param mappedStatement Mybatis mappedStatement
     */
    @SneakyThrows
    private void refactoringSql(String sql, MetaObject metaObject, List<ScopeExecutor> executors, MappedStatement mappedStatement) {
        final SqlCommandType commandType = mappedStatement.getSqlCommandType();
        switch (commandType) {
            case SELECT:
                this.handleSelect(sql, executors, metaObject);
                break;
            case UPDATE:
                this.handleUpdate(sql, executors, metaObject);
                break;
            case DELETE:
                this.handleDelete(sql, executors, metaObject);
                break;
            default:
                break;
        }

    }

    @SneakyThrows
    @SuppressWarnings("DuplicatedCode")
    private void handleDelete(String sql, List<ScopeExecutor> executors, MetaObject metaObject) {
        final Delete delete = (Delete) CCJSqlParserUtil.parse(sql);
        final Expression exp = delete.getWhere();

        // Data scope 条件
        // 更新语句，必须要有对应的条件
        if (null != exp) {
            for (ScopeExecutor executor : executors) {
                final Expression scopeExp = CCJSqlParserUtil.parseCondExpression(executor.sql());
                final AndExpression andExpression = new AndExpression(exp, scopeExp);
                delete.setWhere(andExpression);
            }
            // 修改 SQL
            metaObject.setValue("delegate.boundSql.sql", delete.toString());
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @SneakyThrows
    private void handleUpdate(String sql, List<ScopeExecutor> executors, MetaObject metaObject) {
        final Update update = (Update) CCJSqlParserUtil.parse(sql);
        final Expression exp = update.getWhere();

        // Data scope 条件
        // 更新语句，必须要有对应的条件
        if (null != exp) {
            for (ScopeExecutor executor : executors) {
                final Expression scopeExp = CCJSqlParserUtil.parseCondExpression(executor.sql());
                final AndExpression andExpression = new AndExpression(exp, scopeExp);
                update.setWhere(andExpression);
            }
            // 修改 SQL
            metaObject.setValue("delegate.boundSql.sql", update.toString());
        }
    }

    @SneakyThrows
    @SuppressWarnings("DuplicatedCode")
    private void handleSelect(String sql, List<ScopeExecutor> executors, MetaObject metaObject) {
        final Select select = (Select) CCJSqlParserUtil.parse(sql);
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        final Expression exp = plainSelect.getWhere();
        // Data scope 条件
        for (ScopeExecutor executor : executors) {
            if (Strings.isEmpty(executor.sqlFragment())) {
                continue;
            }
            final Expression scopeExp = CCJSqlParserUtil.parseCondExpression(executor.sql());
            // where 子句当前不存在
            if (null == exp) {
                plainSelect.setWhere(scopeExp);
            } else {
                final AndExpression andExpression = new AndExpression(exp, scopeExp);
                plainSelect.setWhere(andExpression);
            }
        }

        // 修改 SQL
        metaObject.setValue("delegate.boundSql.sql", plainSelect.toString());
    }

    /**
     * 获取原始 SQL
     *
     * @param statementHandler Mybatis statementHandler
     * @return 原始 SQL
     */
    private String parseOriginalSql(StatementHandler statementHandler) {
        return statementHandler.getBoundSql().getSql();
    }

    @SneakyThrows
    private String getScopedKey(Scoped scoped) {
        final Class<? extends ScopedHandler<?>> value = scoped.value();
        final ScopedHandler<?> handler = value.newInstance();
        return handler.key();
    }



    /**
     * 获取实体类类型
     *
     * @param type 范型参数
     * @return 实体类类型
     */
    Class<? extends AncestorDomain> getAncestorDomain(Type type) {
        final Class<? extends Type> clazz = type.getClass();
        try {
            final Field field = clazz.getDeclaredField("actualTypeArguments");
            field.setAccessible(true);
            final Type[] domain = (Type[]) field.get(type);
            for (Type domainType : domain) {
                Class<?> typeClazz = Class.forName(domainType.getTypeName());
                if (AncestorDomain.class.isAssignableFrom(typeClazz)) {
                    // noinspection unchecked
                    return (Class<? extends AncestorDomain>) typeClazz;
                }
            }
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            return null;
        }
        return null;
    }

    /**
     * 获取 Mapper Class 实例
     *
     * @param buf 类路径
     * @return Mapper Class 实例
     */
    private Class<?> getMybatisMapperClass(String buf) {
        final String clazzName = buf.replaceAll("\\.$", "");
        try {
            return Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
