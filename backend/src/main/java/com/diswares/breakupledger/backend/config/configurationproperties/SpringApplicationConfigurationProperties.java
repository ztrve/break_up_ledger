package com.diswares.breakupledger.backend.config.configurationproperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: z_true
 * @date: 2022/7/26 17:20
 * @version: 1.0.0
 */
@ConfigurationProperties(prefix = "spring.application")
@Configuration
@Data
public class SpringApplicationConfigurationProperties {
    private String name;
}
