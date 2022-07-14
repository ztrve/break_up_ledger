package com.diswares.breakupledger.backend.kernel.constants;

import org.springframework.http.HttpStatus;

/**
 * Code 常量
 *
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/2
 */
public enum ResponseCode {

    /**
     * 参数说明
     */
    SUCCESS("E0001", "请求成功", HttpStatus.OK),
    ERROR_SERVICE("E1001", "平台服务异常", HttpStatus.INTERNAL_SERVER_ERROR),
    ERROR_DB("E1002", "服务器数据库异常", HttpStatus.INTERNAL_SERVER_ERROR),
    UNIQUE_KEY("E1003", "唯一值重复录入", HttpStatus.INTERNAL_SERVER_ERROR),
    TRANSFORM_ERROR("E1004", "类型转换错误", HttpStatus.INTERNAL_SERVER_ERROR),
    ERROR_QUERY("E2001", "请求参数异常，缺少必填项", HttpStatus.BAD_REQUEST),
    ERROR_QUERY_FORMAT("E2002", "请求参数异常，参数格式不合法", HttpStatus.BAD_REQUEST),
    NO_LOGIN_ERROR("E4001", "请先登录", HttpStatus.UNAUTHORIZED);

    /**
     * 值
     */
    public String value;
    /**
     * 对应信息
     */
    public String msg;

    public HttpStatus status;

    ResponseCode(String value, String msg, HttpStatus status) {
        this.value = value;
        this.msg = msg;
        this.status = status;
    }
}
