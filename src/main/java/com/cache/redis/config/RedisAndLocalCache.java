package com.cache.redis.config;

import com.cache.redis.config.TwoLevelCacheManager;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: jujun chen
 * @description:
 * @date: 2019/2/15
 */
public class RedisAndLocalCache implements Cache {

    ConcurrentHashMap<Object,Object> localCache = new ConcurrentHashMap<>();

    RedisCache redisCache;

    TwoLevelCacheManager twoLevelCacheManager;

    public RedisAndLocalCache(TwoLevelCacheManager twoLevelCacheManager,RedisCache redisCache) {
        this.twoLevelCacheManager = twoLevelCacheManager;
        this.redisCache = redisCache;
    }

    @Override
    public String getName() {
        return redisCache.getName();
    }

    @Override
    public Object getNativeCache() {
        return redisCache.getNativeCache();
    }

    /**
     * 先从本地缓存中取，有缓存直接返回，没有从redis取回，并放入本地缓存
     * @param key
     * @return
     */
    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper valueWrapper = (ValueWrapper) localCache.get(key);

        if (valueWrapper != null){
            System.out.println("==从本机内存读取缓存");
            return valueWrapper;
        }else {
            valueWrapper = redisCache.get(key);
            System.out.println("==从redis读取缓存");
            if (valueWrapper != null){
                localCache.put(key,valueWrapper);
            }
        }

        return valueWrapper;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper value = get(key);
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException(
                    "Cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return (T) value;
    }

    @Override
    public synchronized <T> T get(Object key, Callable<T> valueLoader) {
        ValueWrapper result = get(key);

        if (result != null) {
            return (T) result.get();
        }

        T value = valueFromLoader(key, valueLoader);
        put(key, value);
        return value;
    }

    @Override
    public void put(Object key, Object value) {
        redisCache.put(key,value);
        clearOtherJVM();
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper valueWrapper = get(key);
        if (valueWrapper == null){
            put(key,value);
            return null;
        }else {
            return valueWrapper;
        }
    }

    @Override
    public void evict(Object key) {
        redisCache.evict(key);
        clearOtherJVM();
    }

    @Override
    public void clear() {
        redisCache.clear();
        clearOtherJVM();
    }

    /**
     * 通知其他节点一级缓存更新
     */
    protected void clearOtherJVM(){
        twoLevelCacheManager.publishMessage(getName());
    }

    /**
     * 清空一级缓存
     */
    public void clearLocalCache(){
        localCache.clear();
    }

    private static <T> T valueFromLoader(Object key, Callable<T> valueLoader) {

        try {
            return valueLoader.call();
        } catch (Exception e) {
            throw new ValueRetrievalException(key, valueLoader, e);
        }
    }

}
