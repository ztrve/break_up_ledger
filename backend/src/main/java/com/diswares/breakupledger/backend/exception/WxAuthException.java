package com.diswares.breakupledger.backend.exception;

import org.springframework.security.core.AuthenticationException;

public class WxAuthException extends AuthenticationException {
    public WxAuthException(String msg) {
        super(msg);
    }
}
