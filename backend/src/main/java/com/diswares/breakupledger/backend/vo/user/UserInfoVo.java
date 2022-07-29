package com.diswares.breakupledger.backend.vo.user;

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
@Data
@EqualsAndHashCode()
public class UserInfoVo implements Serializable {
    private Long id;

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
    private String phone;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
