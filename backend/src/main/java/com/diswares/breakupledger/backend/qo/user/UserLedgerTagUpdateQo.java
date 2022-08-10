package com.diswares.breakupledger.backend.qo.user;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户账本标签库
 * @author z_true
 * @TableName user_ledger_tag
 */
@Data
@EqualsAndHashCode
public class UserLedgerTagUpdateQo implements Serializable {

    /**
     * 账本标签id
     */
    @NotNull
    private Long ledgerTagId;

    /**
     * 是否为默认标签
     *
     * 默认 false
     */
    @NotNull
    private Boolean isDefaultTag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
