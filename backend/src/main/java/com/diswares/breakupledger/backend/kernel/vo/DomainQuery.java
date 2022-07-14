package com.diswares.breakupledger.backend.kernel.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import core.MultipartData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/9/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DomainQuery extends Page<AncestorDomain> {

    /**
     * 查询对象，
     * 内置的对象类型为 {@link QueryOps}
     */
    private Map<String, MultipartData> q;
}
