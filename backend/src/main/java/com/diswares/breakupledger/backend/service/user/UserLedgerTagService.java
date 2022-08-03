package com.diswares.breakupledger.backend.service.user;

import com.diswares.breakupledger.backend.po.user.UserLedgerTag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.diswares.breakupledger.backend.qo.user.UserLedgerTagCreateQo;
import com.diswares.breakupledger.backend.vo.user.UserLedgerTagVo;

import java.util.List;

/**
 *
 * @author z_true
 */
public interface UserLedgerTagService extends IService<UserLedgerTag> {

    /**
     * 获取详情
     *
     * @param id id
     * @return 详情
     */
    UserLedgerTagVo getOneDetail(Long id);

    /**
     * 我的账单标签列表
     *
     * @return 我的账单标签列表
     */
    List<UserLedgerTagVo> myList();

    /**
     * 新建
     *
     * @param userLedgerTagCreateQo qo
     * @return UserLedger详情
     */
    UserLedgerTagVo createOne(UserLedgerTagCreateQo userLedgerTagCreateQo);
}
