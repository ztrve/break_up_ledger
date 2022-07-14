package com.diswares.breakupledger.backend.qo.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 用户创建Qo
 *
 * @author : GTF
 * @version : 1.0
 * @date : 2022/5/31 11:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserCreateQo extends UserBasicQo implements Serializable {
    /**
     * 登录密码
     */
    @NotBlank(message = "密码不能为空!")
    private String password;
}
