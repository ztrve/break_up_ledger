package com.diswares.breakupledger.backend.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: z_true
 * @date: 2022/7/26 14:36
 * @version: 1.0.0
 */
@Getter
@AllArgsConstructor
public enum NoticeDealEnums {
    /**
     * 。。。
     */
    UN_DEAL(0, "未处理"),
    DEAL(1, "处理"),
    ;

    @JsonValue
    @EnumValue
    private final Integer code;

    private final String desc;

    /**
     * 自定义反序列化enum方法
     */
    @JsonCreator
    public static NoticeDealEnums getEnum(Integer code){
        for(NoticeDealEnums item : values()){
            if(item.getCode().equals(code)){
                return item;
            }
        }
        throw new IllegalArgumentException(
                "Cannot convert code " + code + " to category type enums");
    }
}
