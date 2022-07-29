package com.diswares.breakupledger.backend.controller;

import com.diswares.breakupledger.backend.qo.notice.NoticeCreateFriendQo;
import com.diswares.breakupledger.backend.service.NoticeService;
import com.diswares.breakupledger.backend.vo.notice.NoticeVo;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: z_true
 * @date: 2022/7/29 14:54
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping("/friend")
    public NoticeVo createNewFriendNotice(@RequestBody @Validated NoticeCreateFriendQo noticeCreateNewFriendQo) {
        noticeCreateNewFriendQo.setUserCharacteristics(noticeCreateNewFriendQo.getUserCharacteristics().trim());
        return noticeService.createNewFriendNotice(noticeCreateNewFriendQo);
    }
}
