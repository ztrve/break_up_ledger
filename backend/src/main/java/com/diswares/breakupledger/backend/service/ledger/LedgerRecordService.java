package com.diswares.breakupledger.backend.service.ledger;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diswares.breakupledger.backend.enums.LedgerRecordModelEnums;
import com.diswares.breakupledger.backend.po.ledger.LedgerRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.diswares.breakupledger.backend.qo.ledger.LedgerRecordQo;
import com.diswares.breakupledger.backend.vo.ledger.LedgerRecordVo;

/**
 * @author z_true
 */
public interface LedgerRecordService extends IService<LedgerRecord> {

    /**
     * 通过 ID 获取 账单记录详情
     *
     * @param id 账单记录id
     * @return 账单记录详情
     */
    LedgerRecordVo getDetailById(Long id);

    /**
     * 新建一条账本记录
     *
     * @param model          @see LedgerRecordModelEnums
     * @param ledgerRecordQo ledgerRecordQo
     * @return 新建的 账本记录
     */
    LedgerRecordVo createLedgerRecord(LedgerRecordModelEnums model, LedgerRecordQo ledgerRecordQo);

    /**
     * 根据 账本id 获取 账单记录分页数据
     * 时间倒序
     *
     * @param page     分页信息
     * @param ledgerId 账本id
     * @return 账单记录分页数据
     */
    Page<LedgerRecordVo> pageByLedgerId(Page<LedgerRecord> page, Long ledgerId);
}
