package com.diswares.breakupledger.backend.service.ledger;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diswares.breakupledger.backend.po.ledger.Ledger;
import com.diswares.breakupledger.backend.po.ledger.LedgerMember;
import com.diswares.breakupledger.backend.mapper.LedgerMemberMapper;
import com.diswares.breakupledger.backend.po.user.UserInfo;
import com.diswares.breakupledger.backend.util.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author z_true
 * @description 针对表【ledger_member(账本成员)】的数据库操作Service实现
 * @createDate 2022-07-31 21:23:35
 */
@Service
public class LedgerMemberServiceImpl extends ServiceImpl<LedgerMemberMapper, LedgerMember>
        implements LedgerMemberService {

    @Override
    public List<Long> myLedgerIds() {
        UserInfo me = AuthUtil.currentUserInfo();

        LambdaQueryWrapper<LedgerMember> query = new LambdaQueryWrapper<>();
        query.eq(LedgerMember::getMemberId, me.getId());
        List<LedgerMember> ledgerMembers = list(query);
        if (ObjectUtils.isEmpty(ledgerMembers)) {
            return null;
        }
        return ledgerMembers.stream()
                .map(LedgerMember::getLedgerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getMemberIdsByLedgerId(Long ledgerId) {
        LambdaQueryWrapper<LedgerMember> ledgerMemberQuery = new LambdaQueryWrapper<>();
        ledgerMemberQuery.eq(LedgerMember::getLedgerId, ledgerId);
        return list(ledgerMemberQuery)
                .stream()
                .map(LedgerMember::getMemberId)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerMember> getLedgerMembersByLedgerId(Long ledgerId) {
        LambdaQueryWrapper<LedgerMember> ledgerMemberQuery = new LambdaQueryWrapper<>();
        ledgerMemberQuery.eq(LedgerMember::getLedgerId, ledgerId);
        return list(ledgerMemberQuery);
    }

    @Override
    public List<Long> updateLedgerMembers(Ledger ledger, List<Long> memberIds) {
        Assert.notNull(ledger, "账本不存在");
        final Long ledgerId = ledger.getId();
        Assert.isTrue(memberIds.contains(ledger.getOwnerId()), "不可删除拥有人");
        Assert.isTrue(memberIds.contains(ledger.getLeaderId()), "请先修改帐门人");

        LambdaQueryWrapper<LedgerMember> ledgerMemberQuery = new LambdaQueryWrapper<>();
        ledgerMemberQuery.eq(LedgerMember::getLedgerId, ledgerId);
        List<LedgerMember> ledgerMembers = list(ledgerMemberQuery);


        List<LedgerMember> addLedgerMemberList;
        if (!ObjectUtils.isEmpty(ledgerMembers)) {
            List<Long> removeIdList = ledgerMembers.stream()
                    .filter(ledgerMember -> !memberIds.contains(ledgerMember.getMemberId()))
                    .map(LedgerMember::getId)
                    .collect(Collectors.toList());
            if (!ObjectUtils.isEmpty(removeIdList)) {
                removeByIds(removeIdList);
            }
            List<Long> ledgerMemberIds = ledgerMembers.stream().map(LedgerMember::getMemberId).collect(Collectors.toList());
            addLedgerMemberList = memberIds.stream()
                    .filter(memberId -> !ledgerMemberIds.contains(memberId))
                    .map(memberId -> {
                        LedgerMember ledgerMember = new LedgerMember();
                        ledgerMember.setLedgerId(ledgerId);
                        ledgerMember.setMemberId(memberId);
                        return ledgerMember;
                    })
                    .collect(Collectors.toList());
        } else {
            addLedgerMemberList = memberIds.stream()
                    .map(memberId -> {
                        LedgerMember ledgerMember = new LedgerMember();
                        ledgerMember.setLedgerId(ledgerId);
                        ledgerMember.setMemberId(memberId);
                        return ledgerMember;
                    })
                    .collect(Collectors.toList());
        }

        if (!ObjectUtils.isEmpty(addLedgerMemberList)) {
            saveBatch(addLedgerMemberList);
        }
        return getMemberIdsByLedgerId(ledgerId);
    }

    @Override
    public boolean removeByLedgerId(Long ledgerId) {
        LambdaQueryWrapper<LedgerMember> query = new LambdaQueryWrapper<>();
        query.eq(LedgerMember::getLedgerId, ledgerId);
        return remove(query);
    }

    @Override
    public int getLedgerWalletAmount(Long ledgerId) {
        LambdaQueryWrapper<LedgerMember> query = new LambdaQueryWrapper<>();
        query.eq(LedgerMember::getLedgerId, ledgerId);
        List<LedgerMember> ledgerMembers = list(query);

        if (ObjectUtils.isEmpty(ledgerMembers)) {
            return 0;
        }
        return ledgerMembers.stream()
                .mapToInt(LedgerMember::getWalletAmount)
                .sum();
    }

}




