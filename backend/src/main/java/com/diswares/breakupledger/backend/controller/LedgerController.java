package com.diswares.breakupledger.backend.controller;

import com.diswares.breakupledger.backend.qo.ledger.LedgerCreateQo;
import com.diswares.breakupledger.backend.service.LedgerService;
import com.diswares.breakupledger.backend.vo.ledger.LedgerVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ledger")
@RequiredArgsConstructor
public class LedgerController {
    private final LedgerService ledgerService;

    @PostMapping
    public LedgerVo createLedger(@RequestBody LedgerCreateQo ledgerCreateQo) {
        return ledgerService.createLedger(ledgerCreateQo);
    }
}
