package com.diswares.breakupledger.backend.kernel.exception;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/3
 */
public interface BreakFeature {
    /**
     * 断路器功能
     *
     * @param expression 期望执行结果
     * @param message    异常抛出消息
     */
    void expect(boolean expression, String message);
}
