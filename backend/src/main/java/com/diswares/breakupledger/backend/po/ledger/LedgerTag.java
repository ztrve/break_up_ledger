package com.diswares.breakupledger.backend.po.ledger;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 账本标签库
 * @author z_true
 * @TableName ledger_tag
 */
@TableName(value ="ledger_tag")
@Data
public class LedgerTag extends AncestorDomain implements Serializable {
    /**
     * 标签
     */
    private String tag;

    /**
     *
     */
    private Date updateTime;

    /**
     *
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
