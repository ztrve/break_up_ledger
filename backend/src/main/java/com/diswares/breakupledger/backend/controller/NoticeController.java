package com.diswares.breakupledger.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diswares.breakupledger.backend.kernel.proxy.response.annotions.Inclusion;
import com.diswares.breakupledger.backend.po.notice.Notice;
import com.diswares.breakupledger.backend.qo.notice.NoticeCreateFriendQo;
import com.diswares.breakupledger.backend.qo.notice.NoticeDealQo;
import com.diswares.breakupledger.backend.service.NoticeService;
import com.diswares.breakupledger.backend.vo.notice.NoticeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.diswares.breakupledger.backend.kernel.proxy.response.InclusionStrategy.PAGE;

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

    @GetMapping()
    @Inclusion(PAGE)
    public Page<NoticeVo> page(Page<Notice> page) {
        return noticeService.pageOfMine(page);
    }

    @PostMapping("/friend")
    public NoticeVo createNewFriendNotice(@RequestBody @Validated NoticeCreateFriendQo noticeCreateNewFriendQo) {
        noticeCreateNewFriendQo.setUserCharacteristics(noticeCreateNewFriendQo.getUserCharacteristics().trim());
        return noticeService.createNewFriendNotice(noticeCreateNewFriendQo);
    }

    @PostMapping("/deal")
    public NoticeVo deal(@RequestBody @Validated NoticeDealQo noticeDealQo) {
        return noticeService.handleNoticeByType(noticeDealQo);
    }
}
