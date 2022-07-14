package com.diswares.breakupledger.backend.config.redis;

import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 自定义 RedisTemplate
 *
 * NOTICE: 目前只支持 单机版redis
 * NOTICE: 需要自定义其他的 RedisTemplate 按需更改 database 的引入方式
 *         并且修改 Qualifier 的引入方式
 *
 * @author ztrue
 * @date 2021-12-05 0:20
 */
@Configuration
public class RedisExampleConfig {
    @Value("${spring.redis.db.example}")
    private Integer database;

    @Bean
    @Qualifier("exampleRedisTemplate")
    public RedisTemplate<String, Object> otherRedisTemplate(@Qualifier("exampleRedisCollectionFactory") RedisConnectionFactory otherRedisFactory) {
        //常用<String,Object>
        return RedisDefaultConfig.initRedisTemplate(otherRedisFactory);
    }

    @Bean
    @Qualifier("exampleStringRedisTemplate")
    public StringRedisTemplate otherStringRedisTemplate(@Qualifier("exampleRedisCollectionFactory") RedisConnectionFactory otherRedisFactory) {
        return new StringRedisTemplate(otherRedisFactory);
    }

    @Bean
    @Qualifier("exampleRedisCollectionFactory")
    public RedissonConnectionFactory otherRedisFactory (RedissonClient redisson) throws NoSuchFieldException, IllegalAccessException {
        return RedisDefaultConfig.initRedissonConnectionFactory(redisson, database);
    }
}
