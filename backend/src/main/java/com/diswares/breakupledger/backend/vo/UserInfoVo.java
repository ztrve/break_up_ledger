package com.diswares.breakupledger.backend.vo;

import lombok.Data;

/**
 * 用户信息Vo
 *
 * @author : GTF
 * @version : 1.0
 * @date : 2022/5/31 10:54
 */
@Data
public class UserInfoVo {
    private Long id;
    /**
     * 照片
     */
    private String photo;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 名称
     */
    private String name;
    /**
     * 员工编号
     */
    private String account;
}
