package com.diswares.breakupledger.backend.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知 处理结果
 *
 * @author: z_true
 * @date: 2022/7/26 14:36
 * @version: 1.0.0
 */
@Getter
@AllArgsConstructor
public enum NoticeDealResultEnums {
    /**
     * 。。。
     */
    DISAGREE(0, "拒绝"),
    AGREE(1, "同意"),
    ;

    @JsonValue
    @EnumValue
    private final Integer type;

    private final String desc;

    /**
     * 自定义反序列化enum方法
     */
    @JsonCreator
    public static NoticeDealResultEnums getEnum(Integer type){
        for(NoticeDealResultEnums item : values()){
            if(item.getType().equals(type)){
                return item;
            }
        }
        throw new IllegalArgumentException(
                "Cannot convert code " + type + " to category type enums");
    }
}
