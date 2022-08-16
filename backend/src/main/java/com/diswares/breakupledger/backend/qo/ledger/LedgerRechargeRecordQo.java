package com.diswares.breakupledger.backend.qo.ledger;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 账本充值记录 qo
 *
 * @author z_true
 */
@Data
@EqualsAndHashCode()
public class LedgerRechargeRecordQo {
    /**
     * 账本
     */
    @NotNull(message = "账本不存在")
    private Long ledgerId;

    /**
     * 金额 单位分 可为正数或者负数
     */
    @NotNull(message = "金额不得为空")
    @Max(value = 10000000, message = "最多输入十万元")
    @Min(value = 1, message = "最少输入一分")
    private Integer amount;

}
