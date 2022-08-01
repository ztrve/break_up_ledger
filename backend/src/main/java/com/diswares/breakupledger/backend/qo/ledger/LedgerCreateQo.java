package com.diswares.breakupledger.backend.qo.ledger;

import com.diswares.breakupledger.backend.enums.LedgerTypeEnums;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author z_true
 */
@Data
public class LedgerCreateQo {
    /**
     * 账本名
     */
    @NotBlank
    private String name;

    /**
     * 账本类型 0普通账本
     */
    private LedgerTypeEnums type;

    /**
     * 成员可以提交 TRUE可以 FALSE不可以
     */
    private Boolean canMemberCommit;

    @NotEmpty
    private List<Long> ledgerMemberIds;
}
