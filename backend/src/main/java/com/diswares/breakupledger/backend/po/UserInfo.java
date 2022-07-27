package com.diswares.breakupledger.backend.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
 * @TableName user_info
 * @author z_true
 */
@TableName(value ="user_info")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfo extends AncestorDomain implements Serializable {
    /**
     * 用户编号
     */
    private String code;

    /**
     *
     */
    private String nickname;

    /**
     *
     */
    private String avatarUrl;

    /**
     *
     */
    private Object phone;

    /**
     *
     */
    private String wxOpenId;

    /**
     *
     */
    private Date updateTime;

    /**
     *
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
