package com.diswares.breakupledger.backend.kernel.exception;

/**
 * 项目内使用的代码熔断器，
 *
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/3
 */
public class Breaker {

    public static BreakFeature db() {
        return (expression, message) -> {
            if (!expression) {
                throw new DataBaseException(message);
            }
        };
    }
}
