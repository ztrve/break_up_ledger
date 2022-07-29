package com.diswares.breakupledger.backend.qo.notice;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 通知创建建立用户关系Qo
 *
 * @author: z_true
 * @date: 2022/7/29 14:56
 * @version: 1.0.0
 */
@Data
public class NoticeCreateFriendQo {
    /**
     * 用户特征
     *
     * 可以是用户的 手机号 or 编号
     */
    @NotBlank(message = "参数填写有误")
    @Length(message = "参数填写有误", min = 1, max = 64)
    private String userCharacteristics;
}
