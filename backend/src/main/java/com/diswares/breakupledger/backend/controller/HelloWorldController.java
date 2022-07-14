package com.diswares.breakupledger.backend.controller;

import com.diswares.breakupledger.backend.kernel.constants.ResponseCode;
import com.diswares.breakupledger.backend.kernel.proxy.response.InclusionStrategy;
import com.diswares.breakupledger.backend.kernel.proxy.response.annotions.Inclusion;
import com.diswares.breakupledger.backend.kernel.proxy.response.annotions.OriginalResponse;
import com.diswares.breakupledger.backend.kernel.vo.ProtocolResponse;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/27
 */

@RestController
@RequestMapping("/cli-demo")
public class HelloWorldController {

    /**
     * Response 使用子键策略
     * 默认放置在 data 下
     */
    @GetMapping
    public CliQuery query(@Valid CliQuery query) {
        final Integer cycle = query.getCycle();
        // 使用 Assert 抛出异常，kernel层会自动处理
        Assert.state(cycle % 2 != 0, "循环周期不应是偶数");
        return query;
    }


    /**
     * Response 使用子键策略
     * 并将数据放置在 child 下
     */
    @GetMapping("/keyed")
    @Inclusion(value = InclusionStrategy.WITH_KEY, key = "child")
    public CliQuery queryWithKey(@Valid CliQuery query) {
        final Integer cycle = query.getCycle();
        // 使用 Assert 抛出异常，kernel层会自动处理
        Assert.state(cycle % 2 != 0, "循环周期不应是偶数");
        return query;
    }


    /**
     * Response 使用平铺策略
     */
    @GetMapping("tiled")
    @Inclusion(InclusionStrategy.TILED)
    public CliQuery queryTiled(@Valid CliQuery query) {
        final Integer cycle = query.getCycle();
        Assert.state(cycle % 2 != 0, "循环周期不应是偶数");
        return query;
    }

    /**
     * 自定义协议 Response
     */
    @GetMapping("protocol")
    public ProtocolResponse protocolResponse(@Valid CliQuery query) {
        final Integer cycle = query.getCycle();
        // 使用 Assert 抛出异常，kernel层会自动处理
        Assert.state(cycle % 2 != 0, "循环周期不应是偶数");
        return new ProtocolResponse()
                .setCode(ResponseCode.ERROR_DB)
                .setMsg("自定义消息");
    }


    /**
     * 按原样返回不进行处理
     */
    @GetMapping("original")
    @OriginalResponse
    public CliQuery original(@Valid CliQuery query) {
        final Integer cycle = query.getCycle();
        // 使用 Assert 抛出异常，kernel层会自动处理
        Assert.state(cycle % 2 != 0, "循环周期不应是偶数");
        return query;
    }

}
