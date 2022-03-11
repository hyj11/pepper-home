package com.sim.landlord.config;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * @author Liang Chenghao
 * Description: 自定义缓存管理器
 * Date: 2020/12/17
 */
public class CustomCacheManagerConfig extends RedisCacheManager {

    public CustomCacheManagerConfig(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        // 如果一个Redis的key为key#1000表示该key的有效期为1000秒
        String[] strings = StringUtils.delimitedListToStringArray(name, "#");
        name = strings[0];
        if (strings.length > 1) {
            long ttl = Long.parseLong(strings[1]);
            cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(ttl));
        }
        return super.createRedisCache(name, cacheConfig);
    }
}
