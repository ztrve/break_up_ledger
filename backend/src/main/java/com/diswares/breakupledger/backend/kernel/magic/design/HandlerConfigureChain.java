package com.diswares.breakupledger.backend.kernel.magic.design;

/**
 * 配置链
 *
 * @author 4everlynn
 * @version V1.0
 * @date 2021/11/13
 */
public interface HandlerConfigureChain {

    /**
     * 配置链
     *
     * @param handler 测试
     * @return self
     */
    HandlerConfigureChain scope(ScopedHandler<?> handler);
}
