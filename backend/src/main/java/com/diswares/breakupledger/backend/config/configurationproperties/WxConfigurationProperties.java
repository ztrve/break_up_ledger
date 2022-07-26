package com.diswares.breakupledger.backend.config.configurationproperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: z_true
 * @date: 2022/7/26 15:36
 * @version: 1.0.0
 */
@Data
@ConfigurationProperties("env.wx")
@Component
public class WxConfigurationProperties {
    private String appId;
    private String appSecret;
}
