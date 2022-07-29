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
public enum NoticeEnums {
    /**
     * 。。。
     */
    FRIEND_REQUEST("FRIEND_REQUEST", "好友申请", "{}想添加您为好友"),
    ;

    @JsonValue
    @EnumValue
    private final String type;

    private final String name;

    private final String strTmpl;

    /**
     * 自定义反序列化enum方法
     */
    @JsonCreator
    public static NoticeEnums getEnum(String type){
        for(NoticeEnums item : values()){
            if(item.getType().equals(type)){
                return item;
            }
        }
        throw new IllegalArgumentException(
                "Cannot convert code " + type + " to category type enums");
    }
}
