package com.diswares.breakupledger.backend.po.notice;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diswares.breakupledger.backend.enums.NoticeDealEnums;
import com.diswares.breakupledger.backend.enums.NoticeDealResultEnums;
import com.diswares.breakupledger.backend.enums.NoticeEnums;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知
 *
 * @TableName notice
 * @author: z_true
 * @date: 2022/7/26 14:41
 * @version: 1.0.0
 */
@TableName(value ="notice")
@Data
@EqualsAndHashCode(callSuper = true)
public class Notice extends AncestorDomain implements Serializable {
    /**
     * 通知类型
     */
    private NoticeEnums noticeType;

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
}
