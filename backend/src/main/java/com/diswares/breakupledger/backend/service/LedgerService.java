package com.diswares.breakupledger.backend.service;

import com.diswares.breakupledger.backend.po.ledger.Ledger;
import com.baomidou.mybatisplus.extension.service.IService;
import com.diswares.breakupledger.backend.qo.ledger.LedgerCreateQo;
import com.diswares.breakupledger.backend.vo.ledger.LedgerVo;
import org.springframework.web.bind.annotation.RequestBody;

/**
* @author z_true
* @description 针对表【ledger(账本)】的数据库操作Service
* @createDate 2022-07-31 21:23:35
*/
public interface LedgerService extends IService<Ledger> {
    /**
     * 获取账本详情
     * @param ledgerId 账本id
     * @return 账本详情
     */
    LedgerVo getDetailById(Long ledgerId);


    /**
     * 创建账本
     *
     * @param ledgerCreateQo 创建账本 Qo
     * @return 账本详情
     */
    LedgerVo createLedger(LedgerCreateQo ledgerCreateQo);

}
