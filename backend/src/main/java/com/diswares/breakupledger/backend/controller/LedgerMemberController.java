package com.diswares.breakupledger.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.diswares.breakupledger.backend.po.ledger.LedgerMember;
import com.diswares.breakupledger.backend.qo.ledger.LedgerMemberWalletRecordGetOneQo;
import com.diswares.breakupledger.backend.service.ledger.LedgerMemberService;
import com.diswares.breakupledger.backend.service.ledger.LedgerMemberWalletRecordService;
import com.diswares.breakupledger.backend.vo.ledger.LedgerMemberWalletRecordVo;
import com.diswares.breakupledger.backend.vo.ledger.LedgerMemberWalletVo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: z_true
 * @date: 2022/8/4 18:24
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/ledger/member")
@RequiredArgsConstructor
public class LedgerMemberController {
    private final LedgerMemberService ledgerMemberService;

    @GetMapping("/wallet/one")
    public LedgerMemberWalletVo getMemberWallet(@RequestParam Long ledgerId, @RequestParam Long memberId) {
        return ledgerMemberService.getMemberWallet(ledgerId, memberId);
    }

    @GetMapping("/wallet")
    public List<LedgerMemberWalletVo> getMemberWallets(@RequestParam Long ledgerId) {
        return ledgerMemberService.getMemberWallets(ledgerId);
    }

}
