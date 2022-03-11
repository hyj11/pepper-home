package com.sim.landlord.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;

/**
 * @author Liang Chenghao
 * Description: Redis 配置
 * Date: 2018/8/22
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * redis 主机
     */
    @Value("${spring.redis.host}")
    private String host;

    /**
     * redis 密码
     */
    @Value("${spring.redis.password}")
    private String password;

    /**
     * redis  数据库
     */
    @Value("${spring.redis.database}")
    private int database;

    @Value("${spring.cache.redis.time-to-live}")
    private long ttl;

    @Value("${spring.redis.port}")
    private int port;

    /**
     * redis缓存配置
     *
     * @param redisConnectionFactory redis连接
     * @return redis 缓存
     */
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {

        // 设置redis的key和value序列器
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(ttl))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));

        // 自定义缓存管理器
        return new CustomCacheManagerConfig(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory()), configuration);
    }

    /**
     * redisTemplate配置
     *
     * @param redisConnectionFactory redis 连接
     * @return redisTemplate
     */
    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用FastJson自带的序列化工具
        GenericFastJsonRedisSerializer serializer = new GenericFastJsonRedisSerializer();
        // Redis的key为字符串，而不是FastJson里"字符串"
        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        return redisTemplate;

    }

    /**
     * redis 连接配置
     *
     * @return 配置好的redis连接
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        RedisStandaloneConfiguration redisStandaloneConfiguration
                = new RedisStandaloneConfiguration();

        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

        return new LettuceConnectionFactory(redisStandaloneConfiguration);

    }

}
