package com.diswares.breakupledger.backend.vo.notice;

import com.baomidou.mybatisplus.annotation.TableField;
import com.diswares.breakupledger.backend.enums.NoticeDealEnums;
import com.diswares.breakupledger.backend.enums.NoticeDealResultEnums;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: z_true
 * @date: 2022/7/29 16:55
 * @version: 1.0.0
 */
@Data
public class NoticeVo {
    private Long id;

    /**
     * 通知类型
     */
    private String noticeType;

    /**
     * 通知名称
     */
    private String noticeName;

    /**
     * 通知信息
     */
    private String noticeMsg;

    /**
     * 通知数据，用于通知成功后，反射接口获取数据。不对外展示
     */
    private String noticeData;

    /**
     * 发起人
     */
    private Long initiatorId;

    /**
     * 处理人
     */
    private Long handlerId;

    /**
     * 处理状态 0未处理 1已处理
     */
    private NoticeDealEnums dealStatus;

    /**
     * 处理结果 0不同意 1同意
     */
    private NoticeDealResultEnums dealResult;

    /**
     *
     */
    private LocalDateTime updateTime;

    /**
     *
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 关联数据
     */
    private UserInfoVo initiator;
}
