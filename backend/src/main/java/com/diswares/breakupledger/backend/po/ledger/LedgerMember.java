package com.diswares.breakupledger.backend.po.ledger;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账本成员
 * @author z_true
 * @TableName ledger_member
 */
@TableName(value ="ledger_member")
@Data
@EqualsAndHashCode(callSuper = true)
public class LedgerMember extends AncestorDomain implements Serializable {
    /**
     * 账本id
     */
    private Long ledgerId;

    /**
     * 成员用户id
     */
    private Long memberId;

    /**
     * 钱包余额 单位分
     */
    private Integer walletAmount;

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
