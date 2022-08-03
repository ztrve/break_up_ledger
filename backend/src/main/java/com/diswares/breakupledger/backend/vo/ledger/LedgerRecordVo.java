package com.diswares.breakupledger.backend.vo.ledger;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import com.diswares.breakupledger.backend.vo.user.UserInfoVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author z_true
 * @TableName ledger_record
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LedgerRecordVo extends AncestorDomain implements Serializable {

    /**
     * 账本
     */
    private Long ledgerId;

    /**
     * 金额 单位分 可为正数或者负数
     */
    private Integer amount;

    /**
     * 标签
     */
    private String tag;

    /**
     * 描述
     */
    private String extra;

    /**
     *
     */
    private Long creatorId;

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
     * 额外信息
     */
    private UserInfoVo creator;
}
