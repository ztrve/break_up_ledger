package com.diswares.breakupledger.backend.service.ledger;

import com.diswares.breakupledger.backend.po.ledger.LedgerMemberWalletRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.diswares.breakupledger.backend.po.ledger.LedgerRecord;
import com.diswares.breakupledger.backend.qo.ledger.LedgerMemberWalletRecordGetOneQo;
import com.diswares.breakupledger.backend.vo.ledger.LedgerMemberWalletRecordVo;

/**
 *
 * @author z_true
 */
public interface LedgerMemberWalletRecordService extends IService<LedgerMemberWalletRecord> {

    /**
     * 获取详情
     *
     * @param qo qo
     * @return 详情
     */
    LedgerMemberWalletRecordVo getOneDetail(LedgerMemberWalletRecordGetOneQo qo);

    /**
     * 根据 账单记录 创建 账本成员 钱包余额变更记录
     *
     * @param ledgerRecord 账单记录
     */
    void createRecordsByLedgerRecord(LedgerRecord ledgerRecord);

}
