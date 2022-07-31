package com.diswares.breakupledger.backend.vo.ledger;

import com.diswares.breakupledger.backend.enums.LedgerTypeEnums;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LedgerVo {
    /**
     * 账本名
     */
    private String name;

    /**
     * 账本类型 0普通账本
     */
    private LedgerTypeEnums type;

    /**
     * 账本拥有者
     */
    private Long ownerId;

    /**
     * 账本实际管理人
     */
    private Long leaderId;

    /**
     * 成员可以提交 TRUE可以 FALSE不可以
     */
    private Boolean canMemberCommit;

    /**
     *
     */
    private Date updateTime;

    /**
     *
     */
    private Date createTime;

    /**
     * 额外信息
     */
    private UserInfoVo owner;

    private UserInfoVo leader;

    private List<UserInfoVo> members;
}
