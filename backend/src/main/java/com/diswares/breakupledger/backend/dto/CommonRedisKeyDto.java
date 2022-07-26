package com.diswares.breakupledger.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: z_true
 * @date: 2022/7/26 18:32
 * @version: 1.0.0
 */
@Data
@AllArgsConstructor
public class CommonRedisKeyDto {
    private String serverName;
    private String moduleName;
    private String method;
}
