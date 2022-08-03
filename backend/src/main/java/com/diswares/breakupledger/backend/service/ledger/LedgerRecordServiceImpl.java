package com.diswares.breakupledger.backend.service.ledger;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diswares.breakupledger.backend.enums.LedgerRecordModelEnums;
import com.diswares.breakupledger.backend.po.ledger.Ledger;
import com.diswares.breakupledger.backend.po.ledger.LedgerRecord;
import com.diswares.breakupledger.backend.mapper.LedgerRecordMapper;
import com.diswares.breakupledger.backend.po.user.UserInfo;
import com.diswares.breakupledger.backend.qo.ledger.LedgerRecordQo;
import com.diswares.breakupledger.backend.service.user.UserInfoService;
import com.diswares.breakupledger.backend.service.user.UserService;
import com.diswares.breakupledger.backend.util.AuthUtil;
import com.diswares.breakupledger.backend.vo.ledger.LedgerRecordVo;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author z_true
 */
@Service
@RequiredArgsConstructor
public class LedgerRecordServiceImpl extends ServiceImpl<LedgerRecordMapper, LedgerRecord>
        implements LedgerRecordService {
    private final LedgerService ledgerService;

    private final LedgerMemberWalletRecordService ledgerMemberWalletRecordService;

    private final UserInfoService userInfoService;

    @Override
    public LedgerRecordVo getDetailById(Long id) {
        LedgerRecord ledgerRecord = getById(id);
        LedgerRecordVo ledgerRecordVo = new LedgerRecordVo();
        BeanUtils.copyProperties(ledgerRecord, ledgerRecordVo);
        return ledgerRecordVo;
    }

    @Override
    public LedgerRecordVo createLedgerRecord(LedgerRecordModelEnums model, LedgerRecordQo ledgerRecordQo) {
        Ledger ledger = ledgerService.getById(ledgerRecordQo.getLedgerId());
        Assert.notNull(ledger, "账本不存在");
        UserInfo me = AuthUtil.currentUserInfo();
        // 是否有权限可以创建记录
        if (!ledger.getCanMemberCommit()) {
            Assert.isTrue(ledger.getLeaderId().equals(me.getId()), "账本" + ledger.getName() + "仅允许帐门人提交");
        }

        // 创建账单记录
        LedgerRecord ledgerRecord = new LedgerRecord();
        ledgerRecord.setLedgerId(ledgerRecordQo.getLedgerId());
        ledgerRecord.setAmount(model.getCode() * ledgerRecordQo.getAmount());
        ledgerRecord.setTag(ledgerRecordQo.getTag());
        ledgerRecord.setExtra(ledgerRecordQo.getExtra());
        ledgerRecord.setCreatorId(me.getId());
        save(ledgerRecord);

        // 创建成员账户扣款记录
        ledgerMemberWalletRecordService.createRecordsByLedgerRecord(ledgerRecord);
        return getDetailById(ledgerRecord.getId());
    }

    @Override
    public Page<LedgerRecordVo> pageByLedgerId(Page<LedgerRecord> page, Long ledgerId) {
        LambdaQueryWrapper<LedgerRecord> query = new LambdaQueryWrapper<>();
        query.eq(LedgerRecord::getLedgerId, ledgerId);
        page = page(page, query);
        Page<LedgerRecordVo> voPage = new Page<>();
        BeanUtils.copyProperties(page, voPage, "records");

        if (ObjectUtils.isEmpty(page.getRecords())) {
            return voPage;
        }

        List<Long> friendIds = page.getRecords().stream().map(LedgerRecord::getCreatorId).collect(Collectors.toList());
        Map<Long, UserInfoVo> userIdMap = userInfoService.listVoByUserIds(friendIds)
                .stream()
                .collect(Collectors.toMap(UserInfoVo::getId, v -> v, (a, b) -> a));

        List<LedgerRecordVo> voList = page.getRecords().stream()
                .map(po -> {
                    LedgerRecordVo vo = new LedgerRecordVo();
                    BeanUtils.copyProperties(po, vo);
                    UserInfoVo creator = userIdMap.get(vo.getCreatorId());
                    vo.setCreator(creator);
                    return vo;
                })
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }
}




