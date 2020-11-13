package com.haozi.cache.core.manager;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haozi.cache.core.interceptor.CacheInvoker;
import com.haozi.cache.core.interceptor.CacheOperation;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存工厂
 * <p>
 * 实现所有缓存策略
 *
 * @author haozi
 * @date 2020-02-1910:33
 */
public class CacheFactory {

    private RedisCacheWriter defaultRedisCacheWriter;

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    public CacheFactory(RedisCacheWriter defaultRedisCacheWriter) {
        this.defaultRedisCacheWriter = defaultRedisCacheWriter;
    }

    public Cache getCache(CacheOperation cacheOperation) {
        // 删除标识 不创建缓存对象
        if (cacheOperation.isEvict()) {
            return cacheMap.get(cacheOperation.getCacheName());
        }
        return cacheMap.computeIfAbsent(cacheOperation.getCacheName(), (name) -> createCache(cacheOperation));
    }

    private Cache createCache(CacheOperation cacheOperation) {
        boolean localCacheFlag = cacheOperation.getLocalCacheExpire() != null;
        boolean remoteCacheFlag = cacheOperation.getRemoteCacheExpire() != null;
        // 二级缓存模式
        if (localCacheFlag && remoteCacheFlag) {
            LocalCache localCache = new LocalCache(cacheOperation.getLocalCacheExpire());
            RemoteCache remoteCache = new RemoteCache(cacheOperation.getCacheName(), defaultRedisCacheWriter, redisCacheConfiguration(cacheOperation.getCacheName(), cacheOperation.getRemoteCacheExpire()));
            return new ProxyCache(localCache, remoteCache);
        }
        // 远端缓存
        if (remoteCacheFlag) {
            return new RemoteCache(cacheOperation.getCacheName(), defaultRedisCacheWriter, redisCacheConfiguration(cacheOperation.getCacheName(), cacheOperation.getRemoteCacheExpire()));
        }
        // 内存缓存模式
        return new LocalCache(cacheOperation.getLocalCacheExpire());
    }

    /**
     * 二级缓存时使用代理类，协调本地缓存和远端缓存
     */
    private class ProxyCache implements Cache {
        private LocalCache localCache;
        private RemoteCache remoteCache;

        public ProxyCache(LocalCache localCache, RemoteCache remoteCache) {
            this.localCache = localCache;
            this.remoteCache = remoteCache;
        }

        @Override
        public Object get(Object key, CacheInvoker vInvoker) {
            return localCache.get(key, () -> remoteCache.get(key, vInvoker));
        }

        @Override
        public List multiGet(List keys, CacheInvoker vInvoker) {
            return localCache.multiGet(keys, () -> remoteCache.multiGet(keys, vInvoker));
        }

        @Override
        public Map multiGetToMap(List keys, CacheInvoker vInvoker) {
            return localCache.multiGetToMap(keys, () -> remoteCache.multiGetToMap(keys, vInvoker));
        }

        @Override
        public Object evict(Object key, CacheInvoker vInvoker) {
            return localCache.evict(key, () -> remoteCache.evict(key, vInvoker));
        }

        @Override
        public Object multiEvict(List keys, CacheInvoker vInvoker) {
            return localCache.evict(keys, () -> remoteCache.evict(keys, vInvoker));
        }
    }

    /**
     * 构建redis配置项
     * <p>
     * TODO 配置项抽离接口，由具体缓存实现
     *
     * @param cacheName
     * @param ttl
     * @return
     */
    private RedisCacheConfiguration redisCacheConfiguration(String cacheName, long ttl) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        return RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith(cacheName)
                .entryTtl(Duration.ofSeconds(ttl))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
    }
}
