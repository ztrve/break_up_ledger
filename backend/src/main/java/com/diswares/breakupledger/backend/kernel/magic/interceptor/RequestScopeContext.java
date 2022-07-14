package com.diswares.breakupledger.backend.kernel.magic.interceptor;

import com.google.common.base.Strings;
import com.diswares.breakupledger.backend.kernel.constants.ResponseCode;
import com.diswares.breakupledger.backend.kernel.exception.BundledException;
import com.diswares.breakupledger.backend.kernel.magic.design.*;
import com.diswares.breakupledger.backend.kernel.vo.ProtocolResponse;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/11/13
 */
@Data
@Accessors(fluent = true, chain = true)
public class RequestScopeContext implements ScopedContext {

    private Map<String, ScopedHandler<?>> handlers;

    private JdbcTemplate jdbcTemplate;

    /**
     * session 校验器 optional
     */
    private SessionCalibrator calibrator;

    @Override
    public ScopedContext set(String key, String value) {
        return this;
    }

    @Override
    public String get(String key) {
        if (isSessionValid()) {
            final ScopedHandler<?> scopedHandler = handlers.get(key);
            final Object scopes = scopedHandler.scopes();
            if (null != scopes) {
                return scopes.toString();
            }
        }
        return null;
    }

    @Override
    public <T> T get(String key, Class<? extends ScopedHandler<T>> clazz) {
        final ScopedHandler<?> scopedHandler = handlers.get(key);
        if (null != scopedHandler && clazz.isAssignableFrom(scopedHandler.getClass())) {
            return clazz.cast(scopedHandler).scopes();
        }
        return null;
    }

    @Override
    public ScopedContext reset() {
        return this;
    }

    @Override
    public SessionCalibrator validator() {
        return this.calibrator;
    }

    @Override
    public Map<String, ScopedHandler<?>> handlers() {
        return this.handlers;
    }

    @Data
    static class HandlerChain implements HandlerConfigureChain {

        private final Map<String, ScopedHandler<?>> handlers = new HashMap<>();

        @Override
        public HandlerConfigureChain scope(ScopedHandler<?> handler) {
            handlers.put(handler.key(), handler);
            return this;
        }
    }

    @Override
    public ScopedContext configure(IHandlerCallback callback) {
        if (null == this.handlers) {
            this.handlers = new HashMap<>(8);
        }
        final HandlerChain chain = new HandlerChain();
        callback.configure(chain);
        this.handlers().putAll(chain.handlers());
        for (ScopedHandler<?> handler : this.handlers().values()) {
            // 如果处理器内不存在 jdbc template 则为其注入上下文内的实例
            if (null == handler.template()) {
                handler.template(this.jdbcTemplate);
            }
        }
        return this;
    }

    @Override
    public ScopedContext jdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        return this;
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    /**
     * 校验 session 有效性
     *
     * @return session 有效性
     */
    @Override
    public boolean isSessionValid() {
        final HttpServletRequest request = this.getRequest();

        if (null == request) {
            return false;
        }

        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (Strings.isNullOrEmpty(token)) {
            throw new BundledException(
                    new ProtocolResponse()
                            .setCode(ResponseCode.NO_LOGIN_ERROR)
                            .setMsg("Session not found, please login to proceed.")
            );
        }

        if (null != calibrator) {
            // 如果设置了校验器，则从校验器获取状态
            return calibrator.validate(token, request);
        }

        return false;
    }

    /**
     * 获取当前请求上下文
     *
     * @return 获取当前请求上下文
     */
    private synchronized HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == attributes) {
            return null;
        }
        // 开启子线程共享 session
        RequestContextHolder.setRequestAttributes(attributes, true);

        return attributes.getRequest();
    }

}
