package com.diswares.breakupledger.backend.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * @author GTF
 * @date 2022-05-27 11:15
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="user_info",schema = "edwards")
@Data
public class UserInfo extends AncestorDomain implements Serializable {

    /**
     * 唯一标识符，存储用户权限模块唯一标识
     */
    private Long unitCode;

    /**
     * 用户头像，图片访问路径
     */
    private String photo;

    /**
     * 用户昵称，前端展示用户昵称，不展示用户名
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 账户(登录账户名)
     */
    private String account;

    /**
     * 来源
     */
    private String source;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
