package com.diswares.breakupledger.backend.service.notice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.diswares.breakupledger.backend.po.ledger.Ledger;
import com.diswares.breakupledger.backend.po.notice.Notice;
import com.diswares.breakupledger.backend.po.user.UserInfo;
import com.diswares.breakupledger.backend.qo.notice.NoticeCreateFriendQo;
import com.diswares.breakupledger.backend.qo.notice.NoticeDealQo;
import com.diswares.breakupledger.backend.vo.notice.NoticeVo;

import java.util.List;

/**
 * @author: z_true
 * @date: 2022/7/26 14:41
 * @version: 1.0.0
 */
public interface NoticeService extends IService<Notice> {

    /**
     * 分页查询 我的
     *
     * @param page 分页参数
     * @return 分页
     */
    Page<NoticeVo> pageOfMine(Page<Notice> page);

    /**
     * 创建通知 好友申请
     * @param noticeCreateNewFriendQo qo
     * @return 通知信息
     */
    NoticeVo createNewFriendNotice(NoticeCreateFriendQo noticeCreateNewFriendQo);

    /**
     * 根据通知类型 处理通知
     *
     * @param noticeDealQo 通知处理Qo
     * @return 通知信息
     */
    NoticeVo handleNoticeByType(NoticeDealQo noticeDealQo);

    /**
     * 创建 账本邀请 通知
     * @param ledger 账本
     * @param initiator 发起人
     * @param memberIds 成员ids
     */
    void createLedgerInviteNotice(Ledger ledger, UserInfo initiator, List<Long> memberIds);
}
