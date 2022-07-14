package com.diswares.breakupledger.backend.kernel.proxy.response.strategy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.MultipartData;
import com.diswares.breakupledger.backend.kernel.proxy.response.InclusionStrategy;
import com.diswares.breakupledger.backend.kernel.proxy.response.annotions.Inclusion;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import com.diswares.breakupledger.backend.kernel.utils.OrderPlugin;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/2
 */
public class StrategyDispatcher {
    private static final Map<InclusionStrategy, Strategy> S_MAP = new EnumMap<>(InclusionStrategy.class);
    public static ObjectMapper mapper;

    static {
        // 父级 Key 策略实现
        S_MAP.put(InclusionStrategy.WITH_KEY,
                (inclusion, data, response) -> data.include(inclusion.key(), response));

        S_MAP.put(InclusionStrategy.PAGE, (inclusion, data, response) -> {
            if (null != response && IPage.class.isAssignableFrom(response.getClass())) {
                @SuppressWarnings("unchecked")
                IPage<? extends AncestorDomain> d = (IPage<? extends AncestorDomain>) response;
                data.parties(response);
                data.include(inclusion.key(), d.getRecords());
                // 仅保留这几个字段
                data.retain("msg", "code", "total", "size", "current", inclusion.key());
                // 分页数量
                data.include("pages", d.getPages());
                final OrderPlugin orderPlugin = new OrderPlugin();
                orderPlugin.install(data);
                // 使用插件重新定义字段顺序
                orderPlugin.ordered("code", "msg", inclusion.key(), "total", "size", "current", "pages");
                // 清空原有 Key
                data.clear();
                // 重置Key值
                data.parties(orderPlugin.attach());
            }
        });

        // 平铺策略实现
        S_MAP.put(InclusionStrategy.TILED,
                (inclusion, data, response) -> {
                    final String value;
                    try {
                        value = mapper.writeValueAsString(response);
                        data.parties(mapper.readValue(value, Object.class));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 转发消息
     *
     * @param inclusion 注解
     * @param data      响应体构建对象指针
     * @param response  方法响应
     */
    public void dispatch(Inclusion inclusion, MultipartData data, Object response) {
        Strategy h = S_MAP.get(inclusion.value());
        if (null != h) {
            h.include(inclusion, data, response);
        }
    }
}
