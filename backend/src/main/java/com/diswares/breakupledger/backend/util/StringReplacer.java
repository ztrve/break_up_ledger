package com.diswares.breakupledger.backend.util;

import org.slf4j.helpers.MessageFormatter;

/**
 * 字符串替换工具
 *
 * 像 Slf4j 那样的用它
 *
 * @description: V1.0
 * @author: z_true
 * @date: 9/23/21
 */
public class StringReplacer {

    /**
     * 拼接
     *
     * Example:
     *  String str = StringReplacer.build("11{}33", "22");
     *  你将会获得 "112233"
     *
     * @param message 待拼接的字符串
     * @param args 待拼接的参数
     * @return 拼接后的字符串
     */
    public static String build (String message, Object... args) {
        return MessageFormatter.arrayFormat(message, args).getMessage();
    }

}
