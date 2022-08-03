package com.diswares.breakupledger.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diswares.breakupledger.backend.enums.LedgerRecordModelEnums;
import com.diswares.breakupledger.backend.kernel.proxy.response.InclusionStrategy;
import com.diswares.breakupledger.backend.kernel.proxy.response.annotions.Inclusion;
import com.diswares.breakupledger.backend.po.ledger.LedgerRecord;
import com.diswares.breakupledger.backend.qo.ledger.LedgerRecordQo;
import com.diswares.breakupledger.backend.service.ledger.LedgerRecordService;
import com.diswares.breakupledger.backend.vo.ledger.LedgerRecordVo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author z_true
 */
@RestController
@RequestMapping("/ledger/record")
@RequiredArgsConstructor
public class LedgerRecordController {
    private final LedgerRecordService ledgerRecordService;

    @GetMapping
    @Inclusion(InclusionStrategy.PAGE)
    public Page<LedgerRecordVo> pageByLedgerId(Page<LedgerRecord> page, @RequestParam Long ledgerId) {
        return ledgerRecordService.pageByLedgerId(page ,ledgerId);
    }

    @PostMapping
    public LedgerRecordVo createLedgerRecord(@RequestBody @Validated LedgerRecordQo ledgerRecordQo) {
        return ledgerRecordService.createLedgerRecord(LedgerRecordModelEnums.DEDUCT, ledgerRecordQo);
    }

}
