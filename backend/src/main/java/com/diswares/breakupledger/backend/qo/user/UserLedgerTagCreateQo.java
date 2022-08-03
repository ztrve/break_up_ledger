package com.diswares.breakupledger.backend.qo.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户账本标签库
 * @author z_true
 * @TableName user_ledger_tag
 */
@Data
@EqualsAndHashCode
public class UserLedgerTagCreateQo implements Serializable {

    /**
     * 标签
     */
    @NotEmpty
    private String tag;

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
