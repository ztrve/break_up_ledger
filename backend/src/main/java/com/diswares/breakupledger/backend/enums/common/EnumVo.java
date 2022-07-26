package com.diswares.breakupledger.backend.enums.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum 返回 Vo 时的格式
 *
 * 当 code 为 Integer 类型时
 *
 * @author: z_true
 * @date: 2021/11/5 4:57 下午
 * @version: 1.0.0
 */
@AllArgsConstructor
@Getter
public class EnumVo {
    private String name;
    private Integer code;
    private String desc;
}
