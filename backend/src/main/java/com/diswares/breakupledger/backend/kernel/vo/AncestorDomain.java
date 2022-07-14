package com.diswares.breakupledger.backend.kernel.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.diswares.breakupledger.backend.kernel.proxy.annotation.ValidateScope;

import javax.validation.constraints.NotNull;

/**
 * 基础对象，提供主键字段
 *
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/2
 */
@Data
public class AncestorDomain {
    @TableId(type = IdType.AUTO)
    @NotNull(message = "ID 不能为空", groups
            = {ValidateScope.Update.class, ValidateScope.Delete.class})
    @JsonIgnore
    private Long id;
}
