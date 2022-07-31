package com.diswares.breakupledger.backend.helper.notice;

import com.diswares.breakupledger.backend.enums.NoticeEnums;
import com.diswares.breakupledger.backend.po.Notice;

/**
 * 通知处理
 */
public interface NoticeHandler {
    /**
     * 通知类型
     *
     * @return 通知类型
     */
    NoticeEnums noticeType();

    /**
     * 同意的处理回调
     *
     * @param notice 通知信息
     */
    void agreeCall(Notice notice);

    /**
     * 拒绝的处理回调
     *
     * @param notice 通知信息
     */
    void disagreeCall(Notice notice);
}
