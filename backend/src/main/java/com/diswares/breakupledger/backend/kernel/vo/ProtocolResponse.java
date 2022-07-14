package com.diswares.breakupledger.backend.kernel.vo;

import core.MultipartData;
import com.diswares.breakupledger.backend.kernel.constants.ResponseCode;
import com.diswares.breakupledger.backend.kernel.extend.ProtocolDataPlugin;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.util.Strings;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/2
 */
@lombok.Data
@Accessors(chain = true)
public class ProtocolResponse {
    private ResponseCode code = ResponseCode.SUCCESS;
    private String msg;

    public MultipartData toData() {

        // 没有默认消息时，取 code 消息
        if (Strings.isEmpty(msg)) {
            this.msg = code.msg;
        }

        final MultipartData multipartData = new MultipartData();

        // 使用插件
        multipartData
                .plugin(ProtocolDataPlugin.class)
                .code(code.value)
                .msg(msg);

        return multipartData;

    }
}
