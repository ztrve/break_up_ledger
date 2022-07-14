package com.diswares.breakupledger.backend.kernel.magic.annotions;

import com.diswares.breakupledger.backend.kernel.magic.design.ScopedContext;
import com.diswares.breakupledger.backend.kernel.magic.interceptor.RequestScopeContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/11/13
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface MybatisPlusMapperScopes {
    /**
     * 上下文环境
     *
     * @return 上下文环境 class
     */
    Class<? extends ScopedContext> context() default RequestScopeContext.class;
}
