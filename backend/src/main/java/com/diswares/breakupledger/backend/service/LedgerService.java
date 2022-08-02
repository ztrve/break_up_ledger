package com.diswares.breakupledger.backend.service;

import com.diswares.breakupledger.backend.po.ledger.Ledger;
import com.baomidou.mybatisplus.extension.service.IService;
import com.diswares.breakupledger.backend.qo.ledger.LedgerCreateQo;
import com.diswares.breakupledger.backend.qo.ledger.LedgerUpdateQo;
import com.diswares.breakupledger.backend.vo.ledger.LedgerVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
* @author z_true
* @description 针对表【ledger(账本)】的数据库操作Service
* @createDate 2022-07-31 21:23:35
*/
public interface LedgerService extends IService<Ledger> {
    /**
     * 我的账本列表
     *
     * @return 我的账本列表
     */
    List<LedgerVo> myLedgers ();

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

    /**
     * 修改账本
     *
     * @param ledgerUpdateQo 修改账本 Qo
     * @return 账本详情
     */
    LedgerVo updateLedger(LedgerUpdateQo ledgerUpdateQo);

    /**
     * 删除账本
     *
     * @param id 账本id
     * @return 账本详情
     */
    LedgerVo removeLedger(Long id);
}
