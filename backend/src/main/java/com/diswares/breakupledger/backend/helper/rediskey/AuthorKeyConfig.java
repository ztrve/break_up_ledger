package com.diswares.breakupledger.backend.helper.rediskey;

import com.diswares.breakupledger.backend.config.configurationproperties.SpringApplicationConfigurationProperties;
import com.diswares.breakupledger.backend.dto.CommonRedisKeyDto;
import com.diswares.breakupledger.backend.util.StringReplacer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * @author: z_true
 * @date: 2022/7/26 17:13
 * @version: 1.0.0
 */
@Configuration
@RequiredArgsConstructor
public class AuthorKeyConfig {
    private final SpringApplicationConfigurationProperties springApplicationConfigurationProperties;

    public String initLoginKey(String wxUserOpenId) {
        CommonRedisKeyDto comm = new CommonRedisKeyDto(springApplicationConfigurationProperties.getName(), "author", "token-user-profile");
        return StringReplacer.build("{}:{}:{}:{}", comm.getServerName(), comm.getModuleName(), comm.getMethod(), wxUserOpenId);
    }
}
