package com.diswares.breakupledger.backend.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Field;

/**
 * 缺省 RedisTemplate
 *
 * NOTICE: application.yml 中 spring.redis.database 的配置会失效，请自己注入 {@code database}
 * NOTICE: 目前只支持 单机版redis
 *
 * @author ztrue
 * @date 2021-12-05 0:20
 */
@Configuration
public class RedisDefaultConfig {
    @Value("${spring.redis.db.default}")
    private Integer database;

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return initRedisTemplate(redisConnectionFactory);
    }

    @Bean
    @Primary
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory (RedissonClient redisson) throws NoSuchFieldException, IllegalAccessException {
        return initRedissonConnectionFactory(redisson, database);
    }

    protected static RedisTemplate<String, Object> initRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //常用<String,Object>
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置默认序列化方式 可以是：Jackson2JsonRedisSerializer GenericJackson2JsonRedisSerializer
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        //声明字符串序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //设置key以字符串序列化
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        return redisTemplate;
    }

    protected static RedissonConnectionFactory initRedissonConnectionFactory(RedissonClient redisson, Integer database) throws NoSuchFieldException, IllegalAccessException {
        Config configCopy = new Config(redisson.getConfig());
        Class<Config> configClazz = Config.class;
        Field singleServerConfigField = configClazz.getDeclaredField("singleServerConfig");
        singleServerConfigField.setAccessible(true);
        SingleServerConfig singleServerConfig = (SingleServerConfig) singleServerConfigField.get(configCopy);
        singleServerConfig.setDatabase(database);
        RedissonClient redissonClient = Redisson.create(configCopy);
        return new RedissonConnectionFactory(redissonClient);
    }

}
