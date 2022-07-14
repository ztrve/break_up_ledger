package com.diswares.breakupledger.backend.kernel.proxy.response;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/2
 */
public enum InclusionStrategy {
    /**
     * 指定 Response 在自动处理时的策略
     */
    TILED,
    PAGE,
    WITH_KEY
}
