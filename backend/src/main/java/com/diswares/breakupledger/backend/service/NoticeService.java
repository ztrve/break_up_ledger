package com.diswares.breakupledger.backend.service;

import com.diswares.breakupledger.backend.po.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.diswares.breakupledger.backend.qo.notice.NoticeCreateFriendQo;
import com.diswares.breakupledger.backend.vo.notice.NoticeVo;

/**
 * @author: z_true
 * @date: 2022/7/26 14:41
 * @version: 1.0.0
 */
public interface NoticeService extends IService<Notice> {
    /**
     * 创建通知 好友申请
     * @param noticeCreateNewFriendQo qo
     * @return 通知信息
     */
    NoticeVo createNewFriendNotice(NoticeCreateFriendQo noticeCreateNewFriendQo);

}
