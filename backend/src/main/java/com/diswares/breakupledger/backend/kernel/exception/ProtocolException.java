package com.diswares.breakupledger.backend.kernel.exception;

import com.diswares.breakupledger.backend.kernel.constants.ResponseCode;
import com.diswares.breakupledger.backend.kernel.vo.ProtocolResponse;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/11/21
 */
public class ProtocolException extends BundledException {

    public ProtocolException(ResponseCode code, String msg) {
        super(new ProtocolResponse()
                .setMsg(msg)
                .setCode(code)
        );
    }

    private ProtocolException(ProtocolResponse response) {
        super(response);
    }
}
