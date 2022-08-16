package com.diswares.breakupledger.backend.mapper;

import com.diswares.breakupledger.backend.po.ledger.LedgerMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author A
* @description 针对表【ledger_member(账本成员)】的数据库操作Mapper
* @createDate 2022-07-31 21:23:35
* @Entity generator.po.LedgerMember
*/
public interface LedgerMemberMapper extends BaseMapper<LedgerMember> {

    /**
     * 根据 账本id 获取 账本余额
     *
     * @param ledgerId 账本id
     * @return 余额
     */
    List<LedgerMember> getMembers(Long ledgerId);

}




