package com.diswares.breakupledger.backend.kernel.magic.design;

/**
 * 配置链
 *
 * @author 4everlynn
 * @version V1.0
 * @date 2021/11/13
 */
public interface IHandlerCallback {

    /**
     * 配置链
     *
     * @param chain 处理器链
     */
    void configure(HandlerConfigureChain chain);
}
