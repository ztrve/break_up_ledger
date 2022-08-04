package com.diswares.breakupledger.backend.service.ledger;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diswares.breakupledger.backend.po.ledger.LedgerMember;
import com.diswares.breakupledger.backend.po.ledger.LedgerMemberWalletRecord;
import com.diswares.breakupledger.backend.mapper.LedgerMemberWalletRecordMapper;
import com.diswares.breakupledger.backend.po.ledger.LedgerRecord;
import com.diswares.breakupledger.backend.qo.ledger.LedgerMemberWalletRecordGetOneQo;
import com.diswares.breakupledger.backend.vo.ledger.LedgerMemberWalletRecordVo;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author z_true
 */
@Service
@RequiredArgsConstructor
public class LedgerMemberWalletRecordServiceImpl extends ServiceImpl<LedgerMemberWalletRecordMapper, LedgerMemberWalletRecord>
        implements LedgerMemberWalletRecordService {
    private final LedgerMemberService ledgerMemberService;

    @Override
    public LedgerMemberWalletRecordVo getOneDetail(LedgerMemberWalletRecordGetOneQo qo) {
        LambdaQueryWrapper<LedgerMemberWalletRecord> query = new LambdaQueryWrapper<>();
        query.eq(LedgerMemberWalletRecord::getLedgerRecordId, qo.getLedgerRecordId())
                .eq(LedgerMemberWalletRecord::getLedgerMemberId, qo.getLedgerMemberId())
                .last("limit 1");
        LedgerMemberWalletRecord po = getOne(query);
        LedgerMemberWalletRecordVo vo = new LedgerMemberWalletRecordVo();
        BeanUtils.copyProperties(po, vo);
        return vo;
    }

    @Override
    public void createRecordsByLedgerRecord(LedgerRecord ledgerRecord) {
        Assert.notNull(ledgerRecord, "账单记录不存在");
        List<LedgerMember> ledgerMembers = ledgerMemberService.getLedgerMembersByLedgerId(ledgerRecord.getLedgerId());

        int amountCount = 0;
        List<LedgerMemberWalletRecord> ledgerMemberWalletRecords = new ArrayList<>();
        for (int i = 0; i < ledgerMembers.size(); i++) {
            LedgerMember ledgerMember = ledgerMembers.get(i);

            int amount;
            // 由于可能发生精度丢失问题，在遍历时先将前几个成员按人数比例扣减金额，最后一个将精度丢失的几分钱补上
            if (ledgerMembers.size() - 1 == i) {
                // 最后一个特殊处理
                amount = ledgerRecord.getAmount() - amountCount;
            } else {
                amount = ledgerRecord.getAmount() / ledgerMembers.size();
            }
            amountCount += amount;

            Integer prevWalletAmount = ledgerMember.getWalletAmount();
            Integer afterWalletAmount = ledgerMember.getWalletAmount() + amount;

            // 封装 LedgerMemberWalletRecord
            LedgerMemberWalletRecord record = new LedgerMemberWalletRecord();
            record.setLedgerId(ledgerRecord.getLedgerId());
            record.setLedgerMemberId(ledgerMember.getMemberId());
            record.setLedgerRecordId(ledgerRecord.getId());
            record.setAmount(amount);
            record.setTag(ledgerRecord.getTag());
            record.setExtra(ledgerRecord.getExtra());
            record.setPrevWalletAmount(prevWalletAmount);
            record.setAfterWalletAmount(afterWalletAmount);
            record.setCreatorId(ledgerRecord.getCreatorId());
            ledgerMemberWalletRecords.add(record);

            ledgerMember.setWalletAmount(afterWalletAmount);
        }
        Assert.isTrue(amountCount == ledgerRecord.getAmount(), "金额扣除计算有误，金额扣除总数与账单金额不符");

        saveBatch(ledgerMemberWalletRecords);
        ledgerMemberService.updateBatchById(ledgerMembers);
    }
}




