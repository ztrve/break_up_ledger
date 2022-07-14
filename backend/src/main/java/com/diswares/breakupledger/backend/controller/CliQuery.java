package com.diswares.breakupledger.backend.controller;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/27
 */

@Data
public class CliQuery {

    @NotNull(message = "查询时需要附带版本号")
    private String version;

    @Max(value = 20, message = "循环次数不应超过20")
    private Integer cycle;
}
