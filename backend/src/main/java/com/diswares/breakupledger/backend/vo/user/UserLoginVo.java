package com.diswares.breakupledger.backend.vo.user;

import lombok.Data;

/**
 * @author: z_true
 * @date: 2022/7/26 14:44
 * @version: 1.0.0
 */
@Data
public class UserLoginVo {
    private String token;

    private UserInfoVo user;
}
