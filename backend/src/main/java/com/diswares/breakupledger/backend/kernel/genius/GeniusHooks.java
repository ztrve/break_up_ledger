package com.diswares.breakupledger.backend.kernel.genius;

import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/11/21
 */
public interface GeniusHooks<T extends AncestorDomain> {

    /**
     * 创建之前的 hook
     *
     * @param domain 领域数据
     */
    default void beforeCreate(AncestorDomain domain) {
    }

    /**
     * 数据创建完毕的 hook
     *
     * @param domain    带有 ID 领域模型(数据库创建执行完毕)
     * @param isSuccess 是否成功
     */
    default void created(AncestorDomain domain, boolean isSuccess) {
    }

    /**
     * 更新之前的 hook
     *
     * @param domain 领域数据
     */
    default void beforeUpdate(AncestorDomain domain) {
    }

    /**
     * 更新完毕的 hook
     *
     * @param domain    领域数据
     * @param isSuccess 是否成功
     */
    default void updated(AncestorDomain domain, boolean isSuccess) {
    }


    /**
     * 删除之前的 hook
     *
     * @param id 被删除数据的 id
     */
    default void beforeDelete(Long id) {
    }


    /**
     * 是否开启事务
     *
     * @return 是否开启事务
     */
    default boolean transactional() {
        return true;
    }

    /**
     * 删除之后的 hook
     *
     * @param id        被删除数据的 id
     * @param isSuccess 是否成功
     */
    default void deleted(Long id, boolean isSuccess) {
    }
}
