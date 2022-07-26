package com.diswares.breakupledger.backend.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author: z_true
 * @date: 2022/7/26 14:36
 * @version: 1.0.0
 */
@Getter
public enum GenderEnums {
    /**
     * wx
     */
    MALE(0, "男"),
    FEMALE(0, "女"),
    ;

    @JsonValue
    @EnumValue
    private final Integer code;

    private final String desc;

    GenderEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 自定义反序列化enum方法
     */
    @JsonCreator
    public static GenderEnums getEnum(String name){
        return GenderEnums.valueOf(name);
    }
}
