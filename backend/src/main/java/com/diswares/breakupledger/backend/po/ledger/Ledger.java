package com.diswares.breakupledger.backend.po.ledger;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diswares.breakupledger.backend.enums.LedgerTypeEnums;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账本
 * @author z_true
 * @TableName ledger
 */
@TableName(value ="ledger")
@Data
@EqualsAndHashCode(callSuper = true)
public class Ledger extends AncestorDomain implements Serializable {
    /**
     * 账本名
     */
    private String name;

    /**
     * 账本类型 0普通账本
     */
    private LedgerTypeEnums type;

    /**
     * 账本拥有者
     */
    private Long ownerId;

    /**
     * 账本实际管理人
     */
    private Long leaderId;

    /**
     * 成员可以提交 TRUE可以 FALSE不可以
     */
    private Boolean canMemberCommit;

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
