package com.diswares.breakupledger.backend.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author: z_true
 * @date: 2022/7/26 14:41
 * @version: 1.0.0
 */
public class WxAuthException extends AuthenticationException {
    public WxAuthException(String msg) {
        super(msg);
    }
}
