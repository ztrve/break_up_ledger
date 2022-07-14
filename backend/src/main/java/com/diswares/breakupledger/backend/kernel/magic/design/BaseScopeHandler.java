package com.diswares.breakupledger.backend.kernel.magic.design;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/11/21
 */
@Accessors(fluent = true)
@Data
public abstract class BaseScopeHandler<T> implements ScopedHandler<T> {
    private JdbcTemplate template;
}
