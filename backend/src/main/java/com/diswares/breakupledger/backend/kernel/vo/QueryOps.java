package com.diswares.breakupledger.backend.kernel.vo;

import lombok.Data;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/9/2
 */
@Data
public class QueryOps {
    private String eq = "";
    private String like = "";
    private String lt = "";
    private String gt = "";
    private String le;
    private String ge;
    private String [] btw;
}
