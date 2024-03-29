package com.diswares.breakupledger.backend.controller;

import com.diswares.breakupledger.backend.qo.ledger.LedgerCreateQo;
import com.diswares.breakupledger.backend.qo.ledger.LedgerUpdateQo;
import com.diswares.breakupledger.backend.service.ledger.LedgerService;
import com.diswares.breakupledger.backend.vo.ledger.LedgerVo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author z_true
 */
@RestController
@RequestMapping("/ledger")
@RequiredArgsConstructor
public class LedgerController {
    private final LedgerService ledgerService;

    @GetMapping("/list")
    public List<LedgerVo> myLedgers () {
        return ledgerService.myLedgers();
    }

    @GetMapping("/{id}")
    public LedgerVo getOneDetail(@PathVariable Long id) {
        return ledgerService.getDetailById(id);
    }

    @PostMapping
    public LedgerVo createLedger(@RequestBody @Validated LedgerCreateQo ledgerCreateQo) {
        return ledgerService.createLedger(ledgerCreateQo);
    }

    @PutMapping
    public LedgerVo updateLedger(@RequestBody @Validated LedgerUpdateQo ledgerUpdateQo) {
        return ledgerService.updateLedger(ledgerUpdateQo);
    }

    @DeleteMapping("/{id}")
    public LedgerVo removeLedger(@PathVariable Long id) {
        return ledgerService.removeLedger(id);
    }
}
