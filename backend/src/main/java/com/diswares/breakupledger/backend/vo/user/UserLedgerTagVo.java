package com.diswares.breakupledger.backend.vo.user;

import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户账本标签库
 * @author z_true
 * @TableName user_ledger_tag
 */
@Data
@EqualsAndHashCode
public class UserLedgerTagVo implements Serializable {

    /**
     * 账本标签id
     */
    private Long ledgerTagId;

    /**
     * 标签
     */
    private String tag;

    /**
     * 是否为默认标签
     *
     * 默认 false
     */
    private Boolean isDefaultTag;
}
