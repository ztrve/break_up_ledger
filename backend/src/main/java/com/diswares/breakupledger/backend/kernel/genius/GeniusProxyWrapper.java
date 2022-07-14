package com.diswares.breakupledger.backend.kernel.genius;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.google.common.base.Joiner;
import core.MultipartData;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import com.diswares.breakupledger.backend.kernel.constants.ResponseCode;
import com.diswares.breakupledger.backend.kernel.exception.ProtocolException;
import com.diswares.breakupledger.backend.kernel.genius.annotions.Public;
import com.diswares.breakupledger.backend.kernel.proxy.annotation.ValidateScope;
import com.diswares.breakupledger.backend.kernel.proxy.response.InclusionStrategy;
import com.diswares.breakupledger.backend.kernel.proxy.response.annotions.Inclusion;
import com.diswares.breakupledger.backend.kernel.utils.QuickAccess;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import com.diswares.breakupledger.backend.kernel.vo.DomainQuery;
import com.diswares.breakupledger.backend.kernel.vo.ProtocolResponse;
import com.diswares.breakupledger.backend.kernel.vo.QueryOps;
import com.diswares.breakupledger.backend.kernel.plugins.SpreadPlugin;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import plugin.TypePlugin;
import weapon.KeyWeapons;
import weapon.Strings;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.List;

/**
 * 将字典表的接口进行自动注册
 * 减少不必须的重复代码
 *
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/6
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Data
@Accessors(fluent = true)
@RestController
public class GeniusProxyWrapper {
    private final Class<? extends IService<?>> service;
    private final Class<? extends AncestorDomain> domain;
    private ApplicationContext context;
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    /**
     * 钩子
     */
    private final GeniusHooks<?> hooks;

    private IService<AncestorDomain> superService;

    private String uri;

    public GeniusProxyWrapper() {
        this(null, null, null);
    }

    public GeniusProxyWrapper(Class<? extends IService<? extends AncestorDomain>> clazz, Class<? extends AncestorDomain> domain, GeniusHooks<?> hooks) {
        this.service = clazz;
        this.domain = domain;
        this.hooks = hooks;
        this.getService(context);
    }

    private void getService(ApplicationContext context) {
        if (null != context) {
            @SuppressWarnings("rawtypes") final IService instance = context.getBean(service);
            this.superService = instance;
            context.getBean(service);
        }
    }

    @GetMapping("/{id}")
    public AncestorDomain getById(@PathVariable Long id) {
        if (null == superService) {
            getService(this.context);
        }

        AncestorDomain data = this.superService.getById(id);
        Assert.notNull(data, "找不到数据");
        return data;
    }


    @SneakyThrows
    @GetMapping
    @Inclusion(InclusionStrategy.TILED)
    public MultipartData page(DomainQuery query) {

        if (null == superService) {
            getService(this.context);
        }

        final QueryChainWrapper<AncestorDomain> chain = this.superService.query();

        // 处理条件查询
        this.handleCondition(chain, query);

        final IPage data = chain.page(query);

        if (null != data) {
            final MultipartData multipartData = new MultipartData();
            multipartData.plugin().data(data.getRecords());
            multipartData.parties(data);
            // 仅保留这几个字段
            multipartData.retain("data", "total", "size", "current");
            // 分页数量
            multipartData.include("pages", data.getPages());
            return multipartData;
        }

        return new MultipartData();
    }

    @SneakyThrows
    private void handleCondition(QueryChainWrapper<AncestorDomain> chain, DomainQuery query) {
        if (null != query && null != this.domain) {
            // 拼接正确的表名称
            String tableName = getTable(this.domain);
            // 查询条件 Map
            final Map<String, MultipartData> q = query.getQ();
            if (null != q) {
                for (String key : q.keySet()) {
                    // 从 domain 取出 key 对应的字段，若没有则不处理
                    final Field field = this.domain.getDeclaredField(key);
                    // 构建 sql col 名称
                    String colName = KeyWeapons.convertLine(getColName(field));
                    field.setAccessible(true);
                    // 反射成操作对象
                    final QueryOps ops = q.get(key).get(QueryOps.class, true);
                    if (null != ops) {
                        final String eq = ops.getEq();
                        final String ge = ops.getGe();
                        final String like = ops.getLike();
                        final String lt = ops.getLt();
                        final String gt = ops.getGt();
                        final String le = ops.getLe();
                        final String[] btw = ops.getBtw();
                        final String column = Joiner.on("").join(tableName, ".", colName);

                        final MultipartData typeHandler = new MultipartData().parties(ops);

                        // 查询参数通过插件转换
                        this.querySpreadPlugin(q,key,typeHandler);

                        if (Date.class.isAssignableFrom(field.getType())) {
                            typeHandler.install((TypePlugin<String, Date>) input -> {
                                Assert.state(input.length() == 10, "仅支持Unix时间戳");
                                final int timestamp = Integer.parseInt(input);
                                return new Date(timestamp * 1000L);
                            });
                        }

                        if (!Strings.isNullOrEmpty(eq)) {
                            chain.eq(this.accessible(field, AccessType.EQ), column,
                                    typeHandler.getPart(AccessType.EQ.toString()
                                            .toLowerCase(Locale.ROOT), field.getType(), true));
                        }

                        if (!Strings.isNullOrEmpty(like)) {
                            chain.like(this.accessible(field, AccessType.LIKE), column,
                                    typeHandler.getPart(AccessType.LIKE.toString()
                                            .toLowerCase(Locale.ROOT), field.getType(), true));
                        }

                        if (!Strings.isNullOrEmpty(lt)) {
                            chain.lt(this.accessible(field, AccessType.LT), column,
                                    typeHandler.getPart(AccessType.LT.toString()
                                            .toLowerCase(Locale.ROOT), field.getType(), true));
                        }

                        if (!Strings.isNullOrEmpty(gt)) {
                            chain.gt(this.accessible(field, AccessType.GT), column,
                                    typeHandler.getPart(AccessType.GT.toString()
                                            .toLowerCase(Locale.ROOT), field.getType(), true));
                        }

                        if (!Strings.isNullOrEmpty(le)) {
                            chain.le(this.accessible(field, AccessType.LE), column,
                                    typeHandler.getPart(AccessType.LE.toString()
                                            .toLowerCase(Locale.ROOT), field.getType(), true));
                        }

                        if (!Strings.isNullOrEmpty(ge)) {
                            chain.ge(this.accessible(field, AccessType.GE), column,
                                    typeHandler.getPart(AccessType.GE.toString()
                                            .toLowerCase(Locale.ROOT), field.getType(), true));
                        }

                        if (null != btw && btw.length >= 2) {
                            chain.between(this.accessible(field, AccessType.BETWEEN), column, btw[0], btw[1]);
                        }

                        // 根据条件拼接查询
                    }
                    field.setAccessible(false);
                }
            }
        }
    }

    /**
     * 获取sql字段名称
     *
     * @param field 实体类字段
     * @return sql字段名称
     */
    private String getColName(Field field) {
        final TableField tableField = field.getDeclaredAnnotation(TableField.class);
        if (null != tableField && !Strings.isNullOrEmpty(tableField.value())) {
            return tableField.value();
        }
        return field.getName();
    }

    private enum AccessType {
        /**
         * 查询操作符
         */
        EQ,
        LIKE,
        BETWEEN,
        GT,
        LT,
        GE,
        LE
    }

    /**
     * 当前字段可否被操作的判断
     *
     * @param field 字段
     * @param type  操作类型
     * @return 可否被操作
     */
    private boolean accessible(Field field, AccessType type) {
        final Public isPublic = field.getDeclaredAnnotation(Public.class);
        if (null == isPublic) {
            return false;
        }

        switch (type) {
            case EQ:
                return isPublic.eq();
            case LE:
                return isPublic.le();
            case GE:
                return isPublic.ge();
            case LT:
                return isPublic.lt();
            case BETWEEN:
                return isPublic.between();
            case LIKE:
                return isPublic.like();
            case GT:
                return isPublic.gt();
            default:
                return false;
        }
    }

    private String getTable(Class<? extends AncestorDomain> domain) {
        StringBuilder buffer = new StringBuilder();
        final TableName tableName = domain.getDeclaredAnnotation(TableName.class);
        if (null != tableName) {
            final String schema = tableName.schema();
            if (!Strings.isNullOrEmpty(schema)) {
                buffer.append(schema)
                        .append(".");
            }
            buffer.append(tableName.value());
            return buffer.toString();
        }
        return KeyWeapons.convert(domain.getSimpleName());
    }

    @PostMapping
    public AncestorDomain create(@RequestBody MultipartData domain) {

        if (null == domain) {
            return null;
        }

        if (null == superService) {
            getService(this.context);
        }

        this.spreadPlugin(domain);

        AncestorDomain ancestorDomain = domain.get(this.domain, true);

        // 手动校验参数
        final Set<ConstraintViolation<AncestorDomain>> validate = validator.validate(ancestorDomain,
                Default.class, ValidateScope.Create.class);

        this.valid(validate);

        if (null != hooks) {
            hooks().beforeCreate(ancestorDomain);
        }

        DataSourceTransactionManager manager = context.getBean(DataSourceTransactionManager.class);
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        if (null != hooks) {

            // 开启 hook 并且开启事务
            if (hooks.transactional()) {
                transaction(() -> {
                    final boolean isSuccess = this.superService.save(ancestorDomain);
                    hooks.created(ancestorDomain, isSuccess);
                });
            } else {
                this.superService.save(ancestorDomain);
            }
        } else {
            this.superService.save(ancestorDomain);
        }

        return superService.getById(ancestorDomain.getId());
    }

    /**
     * 封装的事务快速方法
     *
     * @param callable 中间执行的函数
     */
    private void transaction(QuickAccess.Callable callable) {
        Assert.notNull(callable, "transaction initialization failed.");

        DataSourceTransactionManager manager = context.getBean(DataSourceTransactionManager.class);
        Assert.notNull(manager, "transaction initialization failed.");

        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // 开启事务
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = manager.getTransaction(definition);

        try {
            callable.call();
            // 提交事务
            manager.commit(transaction);
        } catch (Exception e) {
            // 回滚事务
            manager.rollback(transaction);
            throw new ProtocolException(ResponseCode.ERROR_DB, "transaction was rolled back due to exception.");
        }
    }

    @SneakyThrows
    @PatchMapping
    public ProtocolResponse update(@RequestBody MultipartData domain) {

        if (null == domain) {
            return null;
        }

        if (null == superService) {
            getService(this.context);
        }

        this.spreadPlugin(domain);

        AncestorDomain ancestorDomain = domain.get(this.domain, true);

        // 手动校验参数
        final Set<ConstraintViolation<AncestorDomain>> validate = validator.validate(ancestorDomain, ValidateScope.Update.class);

        this.valid(validate);

        Assert.state(domain.keySet().size() > 1, "更新字段至少大于一个");

        if (null != hooks) {
            hooks.beforeUpdate(ancestorDomain);
        }

        if (null != hooks) {

            // 开启 hook 并且开启事务
            if (hooks.transactional()) {
                transaction(() -> {
                    boolean isSuccess = this.superService.saveOrUpdate(ancestorDomain);
                    hooks.updated(ancestorDomain, isSuccess);
                });
            } else {
                boolean isSuccess = this.superService.saveOrUpdate(ancestorDomain);
                Assert.state(isSuccess, "error when updating.");
            }

        } else {
            boolean isSuccess = this.superService.saveOrUpdate(ancestorDomain);
            Assert.state(isSuccess, "error when updating.");
        }

        return new ProtocolResponse();
    }

    private void valid(Set<ConstraintViolation<AncestorDomain>> validate) {
        final ConstraintViolation[] constraintViolations = validate.toArray(new ConstraintViolation[]{});

        // 校验器长度大于 0， 抛出异常，取第一条校验异常消息
        Assert.state(validate.isEmpty(), constraintViolations.length > 0 ? constraintViolations[0].getMessage() : "");
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Assert.notNull(id, "删除时需要指定ID");

        if (null == superService) {
            getService(this.context);
        }

        if (null != hooks) {
            hooks.beforeDelete(id);
        }

        if (null != hooks) {

            // 开启 hook 并且开启事务
            if (hooks.transactional()) {
                transaction(() -> {
                    final boolean isSuccess = this.superService.removeById(id);
                    hooks.deleted(id, isSuccess);
                });
            } else {
                final boolean isSuccess = this.superService.removeById(id);
                Assert.state(isSuccess, "error when deleting.");
            }
        } else {
            final boolean isSuccess = this.superService.removeById(id);
            Assert.state(isSuccess, "error when deleting.");
        }

    }

    private void spreadPlugin(MultipartData domain){
        SpreadPlugin spreadPlugin = context.getBean(SpreadPlugin.class);
        List<TypePlugin<?, ?>> plugins = spreadPlugin.getPlugins();
        if (plugins.size() > 0){
            for (TypePlugin<?, ?> plugin : plugins) {
                domain.install(plugin);
            }
        }
        Map<Class<?>, TypePlugin<?, ?>> localPlugins = spreadPlugin.getLocalPlugins();
        if (localPlugins.size() > 0){
            AncestorDomain ancestorDomain = domain.get(this.domain, true);
            TypePlugin<?, ?> typePlugin = localPlugins.get(ancestorDomain.getClass());
            if (typePlugin != null){
                domain.install(typePlugin);
            }
        }
    }

    private void querySpreadPlugin(Map<String, MultipartData> q , String key ,MultipartData typeHandler){
        SpreadPlugin spreadPlugin = context.getBean(SpreadPlugin.class);
        List<TypePlugin<?, ?>> plugins = spreadPlugin.getPlugins();
        Map<Class<?>, TypePlugin<?, ?>> localPlugins = spreadPlugin.getLocalPlugins();
        if (plugins.size() > 0){
            for (TypePlugin<?, ?> plugin : plugins) {
                typeHandler.install(plugin);
            }
        }
        for (String domainKey : typeHandler.keySet()) {
            Object o = typeHandler.get(domainKey);
            if (o != null && o != ""){
                AncestorDomain ancestorDomain = typeHandler.get(this.domain, true);
                final MultipartData multipartData = new MultipartData().parties(ancestorDomain);
                multipartData.put(key,typeHandler.get(domainKey));
                if ( localPlugins.size() > 0 ){
                    // 获取到对应的插件
                    TypePlugin<?, ?> typePlugin = localPlugins.get(ancestorDomain.getClass());
                    if (typePlugin != null){
                        multipartData.install(typePlugin);
                    }
                }
                AncestorDomain tempDomain = multipartData.get(this.domain, true);
                final MultipartData tempData = new MultipartData().parties(tempDomain);
                for (String oriKey : q.get(key).keySet()) {
                    if (domainKey.equals(oriKey)){
                        typeHandler.put(domainKey,tempData.get(key));
                    }
                }
            }
        }
    }

}
