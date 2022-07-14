package com.diswares.breakupledger.backend.kernel.genius.annotions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段查询支持
 * @author 4everlynn
 * @version V1.0
 * @date 2021/9/2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Public {
    boolean eq() default false;
    boolean like() default false;
    boolean between() default false;
    boolean lt() default false;
    boolean gt() default false;
    boolean le() default false;
    boolean ge() default false;
}
