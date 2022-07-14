package com.diswares.breakupledger.backend.kernel.genius.annotions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/6
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Export {
    String path();

    boolean page() default true;

    boolean create() default false;

    boolean delete() default false;

    boolean update() default false;

    boolean getFromId() default true;
}
