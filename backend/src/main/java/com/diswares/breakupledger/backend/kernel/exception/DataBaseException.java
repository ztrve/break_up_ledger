package com.diswares.breakupledger.backend.kernel.exception;

import com.diswares.breakupledger.backend.kernel.constants.ResponseCode;
import com.diswares.breakupledger.backend.kernel.vo.ProtocolResponse;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/3
 */
public class DataBaseException extends BundledException {

    DataBaseException(String msg) {
        this(new ProtocolResponse()
                .setCode(ResponseCode.ERROR_DB)
                .setMsg(msg)
        );
    }

    private DataBaseException(ProtocolResponse response) {
        super(response);
        response.setCode(ResponseCode.ERROR_DB);
    }

}
