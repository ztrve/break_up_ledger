package com.diswares.breakupledger.backend.kernel.proxy.response.strategy;


import core.MultipartData;
import com.diswares.breakupledger.backend.kernel.proxy.response.annotions.Inclusion;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/2
 */
public interface Strategy {
    /**
     * 执行填充策略
     *
     * @param inclusion 注解信息
     * @param data      目标 Map
     * @param response  controller 返回的响应
     */
    void include(Inclusion inclusion, MultipartData data, Object response);
}
