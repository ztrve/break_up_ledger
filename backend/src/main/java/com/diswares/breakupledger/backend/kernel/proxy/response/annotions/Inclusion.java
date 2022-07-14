package com.diswares.breakupledger.backend.kernel.proxy.response.annotions;

import com.diswares.breakupledger.backend.kernel.proxy.response.InclusionStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/2
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inclusion {
    /**
     * 指定填充策略
     *
     * @return 填充策略
     */
    InclusionStrategy value() default InclusionStrategy.WITH_KEY;

    /**
     * 返回时候的父对象 Key
     *
     * @return 父对象 Key
     */
    String key() default "data";
}
