package com.diswares.breakupledger.backend.kernel.exception;

import com.diswares.breakupledger.backend.kernel.vo.ProtocolResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BundledException extends RuntimeException{
    private final ProtocolResponse response;

    public BundledException (ProtocolResponse response) {
        super(response.getMsg());
        this.response = response;
    }

}
