package com.diswares.breakupledger.backend.vo.ledger;

import lombok.Data;

/**
 * @author: z_true
 * @date: 2022/8/15 15:30
 * @version: 1.0.0
 */
@Data
public class LedgerMemberWalletVo {
    private Long id;

    private Long ledgerId;

    private Long memberId;

    private Integer amount;
}
