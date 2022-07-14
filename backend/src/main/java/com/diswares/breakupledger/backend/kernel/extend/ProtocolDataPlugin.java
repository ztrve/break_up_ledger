package com.diswares.breakupledger.backend.kernel.extend;

import core.Data;
import support.DataPlugin;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/2
 */
public class ProtocolDataPlugin implements DataPlugin {

    private Data data;

    @Override
    public DataPlugin install(Data data) {
        this.data = data;
        return this;
    }

    public ProtocolDataPlugin msg(String msg) {
        this.data.include("msg", msg);
        return this;
    }

    public ProtocolDataPlugin code(String code) {
        this.data.include("code", code);
        return this;
    }

    @Override
    public Data attach() {
        return this.data;
    }
}
