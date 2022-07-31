package com.diswares.breakupledger.backend.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diswares.breakupledger.backend.po.ledger.LedgerMember;
import com.diswares.breakupledger.backend.mapper.LedgerMemberMapper;
import org.springframework.stereotype.Service;

/**
* @author A
* @description 针对表【ledger_member(账本成员)】的数据库操作Service实现
* @createDate 2022-07-31 21:23:35
*/
@Service
public class LedgerMemberServiceImpl extends ServiceImpl<LedgerMemberMapper, LedgerMember>
    implements LedgerMemberService{

}




