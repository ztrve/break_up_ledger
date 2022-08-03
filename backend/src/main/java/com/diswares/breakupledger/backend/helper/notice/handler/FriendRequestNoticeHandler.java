package com.diswares.breakupledger.backend.helper.notice.handler;

import com.diswares.breakupledger.backend.enums.NoticeEnums;
import com.diswares.breakupledger.backend.helper.notice.NoticeHandler;
import com.diswares.breakupledger.backend.po.friend.Friend;
import com.diswares.breakupledger.backend.po.notice.Notice;
import com.diswares.breakupledger.backend.service.friend.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author z_true
 */
@Component
@RequiredArgsConstructor
public class FriendRequestNoticeHandler implements NoticeHandler {
    private final FriendService friendService;

    @Override
    public NoticeEnums noticeType() {
        return NoticeEnums.FRIEND_REQUEST;
    }

    @Override
    public void agreeCall(Notice notice) {
        List<Friend> friends = new ArrayList<>();
        if (!friendService.u2IsU1Friend(notice.getInitiatorId(), notice.getHandlerId())) {
            Friend f = new Friend();
            f.setLeftUserId(notice.getInitiatorId());
            f.setRightUserId(notice.getHandlerId());
            friends.add(f);
        }
        if (!friendService.u2IsU1Friend(notice.getHandlerId(), notice.getInitiatorId())) {
            Friend f = new Friend();
            f.setLeftUserId(notice.getHandlerId());
            f.setRightUserId(notice.getInitiatorId());
            friends.add(f);
        }
        if (ObjectUtils.isEmpty(friends)) {
            return;
        }
        friendService.saveBatch(friends);
    }

    @Override
    public void disagreeCall(Notice notice) {
        // none
    }
}
