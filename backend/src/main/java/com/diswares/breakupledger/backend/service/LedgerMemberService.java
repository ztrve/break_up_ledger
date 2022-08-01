package com.diswares.breakupledger.backend.service;

import com.diswares.breakupledger.backend.po.ledger.LedgerMember;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author z_true
* @description 针对表【ledger_member(账本成员)】的数据库操作Service
* @createDate 2022-07-31 21:23:35
*/
public interface LedgerMemberService extends IService<LedgerMember> {

    /**
     * 我的账本ID
     *
     * @return 我的账本ID
     */
    List<Long> myLedgerIds();

}
