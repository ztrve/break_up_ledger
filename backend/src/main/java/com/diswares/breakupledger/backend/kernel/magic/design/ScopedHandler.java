package com.diswares.breakupledger.backend.kernel.magic.design;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/11/12
 */
public interface ScopedHandler<T> {
    /**
     * 进行处理的 Key
     *
     * @return 进行处理的 Key
     */
    String key();

    /**
     * 处理逻辑
     *
     * @return 返回的 Scope 数据集合
     */
    SqlBuilder<T> builder();

    /**
     * 是否放行
     *
     * @return 是否放行
     */
    default boolean skip() {
        return false;
    }

    /**
     * 必须采用 template 进行数据库访问，
     * 避免 mapper -> 拦截器 -> mapper 的引起的循环、StackOverflow
     *
     * @return JdbcTemplate 实例
     */
    JdbcTemplate template();


    /**
     * 设置 JdbcTemplate
     *
     * @param template JdbcTemplate
     * @return self
     */
    ScopedHandler<T> template(JdbcTemplate template);

    /**
     * 获取 scopes 的范围，
     *
     * @return scope 字段取值集合
     */
    T scopes();

    /**
     * 获取当前请求
     *
     * @return 当前请求
     */
    default HttpServletRequest getCurrentSession() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (null == attributes) {
            return null;
        }
        RequestContextHolder.setRequestAttributes(attributes, true);
        return ((ServletRequestAttributes) attributes).getRequest();
    }
}
