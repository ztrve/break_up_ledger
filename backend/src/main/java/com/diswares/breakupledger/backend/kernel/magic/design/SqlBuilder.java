package com.diswares.breakupledger.backend.kernel.magic.design;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/11/18
 */
public interface SqlBuilder<T> {
    /**
     * 构建 SQL
     *
     * @param arg 参数
     * @return 构建好的 SQL
     */
    String sql(T arg);
}
