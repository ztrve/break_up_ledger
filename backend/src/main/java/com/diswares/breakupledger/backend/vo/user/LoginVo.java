package com.diswares.breakupledger.backend.vo.user;

import com.diswares.breakupledger.backend.po.UserInfo;
import lombok.Data;

/**
 * @author: z_true
 * @date: 2022/7/26 14:44
 * @version: 1.0.0
 */
@Data
public class LoginVo {
    private String token;

    private UserInfo user;
}
