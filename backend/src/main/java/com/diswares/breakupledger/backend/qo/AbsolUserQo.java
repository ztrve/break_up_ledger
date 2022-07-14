package com.diswares.breakupledger.backend.qo;

import lombok.Data;

/**
 * @author jbjia
 * @version V1.0
 * @title
 * @description
 * @date 2021/11/23 10:49
 */
@Data
public class AbsolUserQo {
    /**
     * 用户主键ID
     */
    private Long id;
    /**
     * 用户姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idNo;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 账户
     */
    private String account;
    /**
     * 头像
     */
    private String image;
    /**
     * 来源
     */
    private String source;

}
