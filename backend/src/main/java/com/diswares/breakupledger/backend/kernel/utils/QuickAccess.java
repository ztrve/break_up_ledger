package com.diswares.breakupledger.backend.kernel.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 提供联合的快捷 API 操作
 *
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/4
 */
@Slf4j
public class QuickAccess {

    public interface Callable {
        /**
         * 调用
         */
        void call();
    }

    @SneakyThrows
    public static void run(Callable callable) {
        final long start = System.currentTimeMillis();
        if (null != callable) {
            callable.call();
            log.info("Method run {} ms", System.currentTimeMillis() - start);
        }
    }


}
