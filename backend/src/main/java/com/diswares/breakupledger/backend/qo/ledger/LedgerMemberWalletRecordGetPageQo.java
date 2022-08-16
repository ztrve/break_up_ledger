package com.diswares.breakupledger.backend.qo.ledger;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 账本成员钱包记录
 * @author z_true
 * @TableName ledger_member_wallet_record
 */
@Data
@EqualsAndHashCode
public class LedgerMemberWalletRecordGetPageQo implements Serializable {
    /**
     * 账本成员id
     */
    @NotNull
    private Long ledgerMemberId;

    /**
     * 账单记录id
     */
    @NotNull
    private Long ledgerId;
}
