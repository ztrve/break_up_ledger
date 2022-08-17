package com.diswares.breakupledger.backend.service.ledger;

import com.diswares.breakupledger.backend.po.ledger.Ledger;
import com.diswares.breakupledger.backend.po.ledger.LedgerMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.diswares.breakupledger.backend.vo.ledger.LedgerMemberWalletVo;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author z_true
 * @description 针对表【ledger_member(账本成员)】的数据库操作Service
 * @createDate 2022-07-31 21:23:35
 */
public interface LedgerMemberService extends IService<LedgerMember> {

    /**
     * 获取账本 成员钱包
     *
     * @param ledgerId 账本id
     * @return 成员钱包
     */
    List<LedgerMemberWalletVo> getMemberWallets(Long ledgerId);

    /**
     * 成员钱包信息
     *
     * @param ledgerId 账本
     * @param memberId 成员
     * @return 成员钱包信息
     */
    LedgerMemberWalletVo getMemberWallet(Long ledgerId, Long memberId);

    /**
     * 我的账本ID
     *
     * @return 我的账本ID
     */
    List<Long> myLedgerIds();

    /**
     * 根据 账本id 获取所有 成员id
     *
     * @param ledgerId 账本id
     * @return 账本中所有 成员id
     */
    List<Long> getMemberIdsByLedgerId(Long ledgerId);

    /**
     * 根据 账本id 获取所有 成员
     *
     * @param ledgerId 账本id
     * @return 成员信息
     */
    List<LedgerMember> getLedgerMembersByLedgerId(Long ledgerId);

    /**
     * 更新账本成员
     *
     * @param ledger    账本
     * @param memberIds 成员ids
     * @return 修改后的 LedgerMember
     */
    List<Long> updateLedgerMembers(Ledger ledger, List<Long> memberIds);

    /**
     * 通过 账本id 删除所有账单记录
     *
     * @param ledgerId 账单id
     * @return true删除成功
     */
    boolean removeByLedgerId(Long ledgerId);

    /**
     * 通过 账本id 获取 账本钱包余额
     *
     * 单位分
     *
     * @param ledgerId 账本id
     * @return 账本钱包余额 单位分
     */
    int getLedgerWalletAmount(Long ledgerId);

    /**
     * 根据 账本id 获取 账本余额
     *
     * @param ledgerId 账本id
     * @return 余额
     */
    Integer getLedgerAmount(Long ledgerId);

}
