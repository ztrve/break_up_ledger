package com.diswares.breakupledger.backend.kernel.magic.annotions;

import com.diswares.breakupledger.backend.kernel.magic.design.ScopedHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注字段为 Scope 限制字段
 *
 * @author 4everlynn
 * @version V1.0
 * @date 2021/11/13
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scoped {
    Class<? extends ScopedHandler<?>> value();

    String key() default "";
}

