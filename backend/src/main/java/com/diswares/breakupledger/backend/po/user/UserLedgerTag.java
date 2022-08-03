package com.diswares.breakupledger.backend.po.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 用户账本标签库
 * @author z_true
 * @TableName user_ledger_tag
 */
@TableName(value ="user_ledger_tag")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserLedgerTag extends AncestorDomain implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 标签
     */
    private String tag;

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
