package com.diswares.breakupledger.backend.kernel.proxy.response.annotions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加了此注册后，方法按原样返回
 * 不再触发 ProtocolResponse 逻辑
 *
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/27
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OriginalResponse {
}
