package com.diswares.breakupledger.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diswares.breakupledger.backend.po.ledger.LedgerMember;
import com.diswares.breakupledger.backend.mapper.LedgerMemberMapper;
import com.diswares.breakupledger.backend.po.user.UserInfo;
import com.diswares.breakupledger.backend.util.AuthUtil;
import org.springframework.stereotype.Service;
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
    implements LedgerMemberService{

    @Override
    public List<Long> myLedgerIds() {
        UserInfo me = AuthUtil.currentUserInfo();

        LambdaQueryWrapper<LedgerMember> query = new LambdaQueryWrapper<>();
        query.eq(LedgerMember::getMemberId, me.getId());
        List<LedgerMember> ledgerMembers = list(query);
        if (ObjectUtils.isEmpty(ledgerMembers)){
            return null;
        }
        return ledgerMembers.stream()
                .map(LedgerMember::getLedgerId)
                .collect(Collectors.toList());
    }
}




