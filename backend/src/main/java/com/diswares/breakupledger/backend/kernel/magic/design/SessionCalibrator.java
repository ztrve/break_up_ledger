package com.diswares.breakupledger.backend.kernel.magic.design;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/11/17
 */
public interface SessionCalibrator {
    /**
     * 校验
     *
     * @param authorization token
     * @param request       http request 用于自定义校验规则
     * @return 是否校验通过
     */
    boolean validate(String authorization, HttpServletRequest request);
}
