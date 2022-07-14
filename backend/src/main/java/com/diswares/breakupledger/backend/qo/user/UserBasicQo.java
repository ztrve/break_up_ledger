package com.diswares.breakupledger.backend.qo.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 用户基本信息
 *
 * @author : GTF
 * @version : 1.0
 * @date : 2022/6/1 16:52
 */
@Data
public class UserBasicQo {
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String name;
    /**
     * 手机号
     */
    @Pattern(regexp = "^$|^(\\d{3}-\\d{7,8}|\\d{4}-\\d{7,8})|((?:(?:\\+|00)86)?1[3-9]\\d{9})$", message = "请输入正确的手机号码或带区号的固话号码")
    private String phone;
    /**
     * 分配用户角色
     */
    private String type;
    /**
     * 身份证号
     */
    @Pattern(regexp="^$|^(\\d{8}(0\\d|10|11|12)([0-2]\\d|30|31)\\d{3}$)|(^\\d{6}(18|19|20)\\d{2}(0\\d|10|11|12)([0-2]\\d|30|31)\\d{3}(\\d|X|x))$",
            message = "请输入正确的身份证号码!")
    private String idCard;
    /**
     * 照片
     */
    private String photo;
    /**
     * 账户
     */
    @NotBlank(message = "账户不能为空")
    private String account;
}
