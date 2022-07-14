package com.diswares.breakupledger.backend.kernel.utils;

import core.Data;
import core.MultipartData;
import support.DataPlugin;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/10/27
 */
public class OrderPlugin implements DataPlugin {
    private Data cache;
    private final MultipartData target = new MultipartData();

    @Override
    public DataPlugin install(Data data) {
        this.cache = data;
        return this;
    }

    public DataPlugin ordered (String... keys) {
        for (String key : keys) {
            if (cache.containsKey(key)) {
                target.put(key, cache.get(key));
            }
        }
        return this;
    }

    @Override
    public Data attach() {
        return target;
    }
}
