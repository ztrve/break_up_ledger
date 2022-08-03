package com.diswares.breakupledger.backend.po.ledger;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账本成员钱包记录
 * @author z_true
 * @TableName ledger_member_wallet_record
 */
@TableName(value ="ledger_member_wallet_record")
@Data
@EqualsAndHashCode(callSuper = true)
public class LedgerMemberWalletRecord extends AncestorDomain implements Serializable {
    /**
     * 账本id
     */
    private Long ledgerId;

    /**
     * 账本成员id
     */
    private Long ledgerMemberId;

    /**
     * 记账金额 单位分 可正可副
     */
    private Integer amount;

    /**
     * 标签
     */
    private String tag;

    /**
     * 描述
     */
    private String extra;

    /**
     * 记账前钱包余额 单位分
     */
    private Integer prevWalletAmount;

    /**
     * 记账后钱包余额 单位分
     */
    private Integer afterWalletAmount;

    /**
     * 记录人id
     */
    private Long creatorId;

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
