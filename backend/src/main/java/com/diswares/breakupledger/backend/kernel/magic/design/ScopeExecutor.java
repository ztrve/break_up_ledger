package com.diswares.breakupledger.backend.kernel.magic.design;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.diswares.breakupledger.backend.kernel.magic.annotions.Scoped;
import lombok.Data;
import lombok.experimental.Accessors;
import weapon.KeyWeapons;

import java.lang.reflect.Field;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/11/21
 */

@Data
@Accessors(fluent = true)
public class ScopeExecutor {
    private String table;
    private String sqlFragment;
    private Field field;

    public String sql() {
        String column = this.getColumn(field);
        return Joiner.on("").join(table, ".", column, sqlFragment);
    }

    private String getColumn(Field field) {
        final Scoped scoped = field.getDeclaredAnnotation(Scoped.class);

        if (null != scoped && !Strings.isNullOrEmpty(scoped.key())) {
            return KeyWeapons.convertLine(scoped.key());
        }

        return KeyWeapons.convertLine(field.getName());
    }
}
