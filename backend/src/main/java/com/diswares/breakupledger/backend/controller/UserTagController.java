package com.diswares.breakupledger.backend.controller;


import com.diswares.breakupledger.backend.qo.user.UserLedgerTagCreateQo;
import com.diswares.breakupledger.backend.service.user.UserLedgerTagService;
import com.diswares.breakupledger.backend.vo.user.UserLedgerTagVo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: z_true
 * @date: 2022/7/26 14:41
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/user/ledger/tag")
@RequiredArgsConstructor
public class UserTagController {
    private final UserLedgerTagService userLedgerTagService;

    @GetMapping("/list")
    public List<UserLedgerTagVo> listVo() {
        return userLedgerTagService.myList();
    }

    @PostMapping
    public UserLedgerTagVo createOne (@RequestBody @Validated UserLedgerTagCreateQo userLedgerTagCreateQo){
        return userLedgerTagService.createOne(userLedgerTagCreateQo);
    }

}
