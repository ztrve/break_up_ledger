package com.diswares.breakupledger.backend.po.friend;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 朋友关系
 *
 * 一对正常的朋友关系，需要有两条Friend维护。
 * 她们彼此互为朋友，才能视为朋友关系成立
 *
 * @TableName friend
 * @author: z_true
 * @date: 2022/7/26 14:41
 * @version: 1.0.0
 */
@TableName(value ="friend")
@Data
@EqualsAndHashCode(callSuper = true)
public class Friend extends AncestorDomain implements Serializable {
    /**
     * 主用户
     */
    private Long leftUserId;

    /**
     * 对向用户
     */
    private Long rightUserId;

    /**
     *
     */
    private LocalDateTime updateTime;

    /**
     *
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
