package com.diswares.breakupledger.backend.qo.user;

import com.diswares.breakupledger.backend.qo.GeneralQo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息查询对象
 *
 * @author : GTF
 * @version : 1.0
 * @date : 2022/6/1 17:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfoQo extends GeneralQo {
    /**
     * 部门id
     */
    private Long deptId;
    /**
     * 职位id
     */
    private Long positionId;
}
