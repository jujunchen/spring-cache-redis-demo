package com.cache.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: jujun chen
 * @description:
 * @date: 2019/2/15
 */
public class TwoLevelCacheManager extends RedisCacheManager {

    RedisTemplate redisTemplate;

    @Value("${spring.cache.redis.topic:cache}")
    String topicName;

    public TwoLevelCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, RedisTemplate redisTemplate) {
        super(cacheWriter, defaultCacheConfiguration);
        this.redisTemplate = redisTemplate;
    }

    /**
     * @param cache
     * @return RedisAndLocalCache 实现了Cache接口，替代RedisCache
     */
    @Override
    protected Cache decorateCache(Cache cache) {
        return new RedisAndLocalCache(this, (RedisCache) cache);
    }

    /**
     * 发送广播
     * @param cacheName
     */
    public void publishMessage(String cacheName){
        System.out.println("...发送清除缓存消息");
        redisTemplate.convertAndSend(topicName,cacheName);
    }

    /**
     * 接收消息清空本地缓存
     * @param cacheName
     */
    public void receiver(String cacheName){
        System.out.println("...接收清除缓存消息");
        RedisAndLocalCache cache = (RedisAndLocalCache) this.getCache(cacheName);
        if (cache != null){
            cache.clearLocalCache();
        }
    }

}
