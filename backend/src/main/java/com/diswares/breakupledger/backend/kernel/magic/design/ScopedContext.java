package com.diswares.breakupledger.backend.kernel.magic.design;

import core.MultipartData;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 范围数据上下文
 *
 * @author 4everlynn
 * @version V1.0
 * @date 2021/11/13
 */
public interface ScopedContext {
    /**
     * 保存数据
     *
     * @param key   key 值
     * @param value value 值
     * @return self
     */
    ScopedContext set(String key, String value);

    /**
     * 获取数据
     *
     * @param key key 值
     * @return self
     */
    String get(String key);

    /**
     * 根据key值获取对应类型的返回数据
     *
     * @param key   key值
     * @param clazz 类名
     * @param <T>   泛型
     * @return 对应 Scope
     */
    <T> T get(String key, Class<? extends ScopedHandler<T>> clazz);


    /**
     * 获取 Optional 对象
     *
     * @param key key
     * @return 值
     */
    default Optional<String> optional(String key) {
        return Optional.ofNullable(get(key));
    }

    /**
     * 根据key值获取对应类型的返回数据
     *
     * @param key   key值
     * @param clazz 类名
     * @param <T>   泛型
     * @return 对应 Scope
     */
    default <T> Optional<T> optional(String key, Class<? extends ScopedHandler<T>> clazz) {
        return Optional.ofNullable(get(key, clazz));
    }

    /**
     * 获取当前存储的所有权限数据
     *
     * @return 权限数据
     */
    default Map<String, Object> entries() {
        MultipartData data = new MultipartData();
        final Set<Map.Entry<String, ScopedHandler<?>>> entries = handlers().entrySet();
        for (Map.Entry<String, ScopedHandler<?>> entry : entries) {
            final ScopedHandler<?> value = entry.getValue();
            data.put(value.key(), value.scopes());
        }
        return data;
    }

    /**
     * 重置状态
     *
     * @return self
     */
    ScopedContext reset();

    /**
     * session 校验器
     *
     * @return 校验器
     */
    SessionCalibrator validator();

    /**
     * 获取字段处理器
     *
     * @return 字段处理器集合
     */
    Map<String, ScopedHandler<?>> handlers();

    /**
     * 配置链
     *
     * @param callback 以回调方式返回配置链
     * @return self
     */
    ScopedContext configure(IHandlerCallback callback);

    /**
     * 获取 jdbc template
     *
     * @return JdbcTemplate
     */
    JdbcTemplate getJdbcTemplate();


    /**
     * 模板
     * @param  template Spring 自动注入的 template
     * @return self
     */
    ScopedContext jdbcTemplate(JdbcTemplate template);

    /**
     * 会话有效判断
     *
     * @return 是否有效的会话
     */
    boolean isSessionValid();
}
