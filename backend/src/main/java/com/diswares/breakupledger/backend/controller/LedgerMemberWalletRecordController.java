package com.diswares.breakupledger.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.diswares.breakupledger.backend.qo.ledger.LedgerMemberWalletRecordGetOneQo;
import com.diswares.breakupledger.backend.service.ledger.LedgerMemberWalletRecordService;
import com.diswares.breakupledger.backend.vo.ledger.LedgerMemberWalletRecordVo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: z_true
 * @date: 2022/8/4 18:24
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/ledger/member/wallet/record")
@RequiredArgsConstructor
public class LedgerMemberWalletRecordController {
    private final LedgerMemberWalletRecordService ledgerMemberWalletRecordService;

    @GetMapping("/one")
    public LedgerMemberWalletRecordVo getOne(@Validated LedgerMemberWalletRecordGetOneQo qo) {
        return ledgerMemberWalletRecordService.getOneDetail(qo);
    }
}
