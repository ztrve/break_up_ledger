package com.diswares.breakupledger.backend.qo.ledger;

import com.diswares.breakupledger.backend.enums.LedgerTypeEnums;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author z_true
 */
@Data
public class LedgerCreateQo {
    /**
     * 账本名
     */
    @NotBlank
    @Length(min = 1, max = 10, message = "账本名称不合法，请输入1-10个字符")
    private String name;

    /**
     * 账本类型 0普通账本
     */
    private LedgerTypeEnums type;

    /**
     * 成员可以提交 TRUE可以 FALSE不可以
     */
    private Boolean canMemberCommit;

    @NotEmpty
    @Size(min = 1, max = 9, message = "成员不合法，账本中至少存在一个成员")
    private List<Long> memberIds;
}
