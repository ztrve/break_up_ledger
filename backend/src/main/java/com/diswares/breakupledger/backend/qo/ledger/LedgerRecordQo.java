package com.diswares.breakupledger.backend.qo.ledger;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author z_true
 */
@Data
@EqualsAndHashCode()
public class LedgerRecordQo implements Serializable {
    /**
     * 账本
     */
    @NotNull
    private Long ledgerId;

    /**
     * 金额 单位分 可为正数或者负数
     */
    @NotNull
    @Max(value = 10000000, message = "最多输入十万元")
    @Min(value = 1, message = "最少输入一分")
    private Integer amount;

    /**
     * 标签
     */
    @NotNull
    private String tag;

    /**
     * 描述
     */
    private String extra;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
