package com.diswares.breakupledger.backend.qo.notice;

import com.diswares.breakupledger.backend.enums.NoticeDealResultEnums;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author z_true
 */
@Data
public class NoticeDealQo {
    @NotNull
    private NoticeDealResultEnums dealResult;

    @NotNull
    private Long noticeId;
}
