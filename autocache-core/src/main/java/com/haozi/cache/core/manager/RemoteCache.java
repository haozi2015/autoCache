package com.haozi.cache.core.manager;

import com.haozi.cache.core.interceptor.CacheInvoker;
import com.haozi.cache.core.util.CacheUtil;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.util.*;

/**
 * 远端缓存
 *
 * @author haozi
 */
public class RemoteCache extends RedisCache implements Cache {

    public RemoteCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
    }

    @Override
    public Object get(Object key, CacheInvoker vInvoker) {
        ValueWrapper result = get(key);

        if (result != null) {
            return result.get();
        }
        Object value = vInvoker.invoke();
        put(key, value);
        return value;
    }

    @Override
    public List multiGet(List keys, CacheInvoker vInvoker) {
        List matchData = new ArrayList(keys.size());
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            ValueWrapper valueWrapper = super.get(key);
            if (valueWrapper != null) {
                matchData.add(valueWrapper.get());
                iterator.remove();
            }
        }

        if (keys.isEmpty()) {
            return matchData;
        }
        List invokeResult = (List) vInvoker.invoke();
        invokeResult.forEach(obj -> {
            Object key = CacheUtil.getCacheFieldValue(obj);
            if (key == null) {
                throw new IllegalArgumentException("key is null. class " + obj.getClass() + " data " + obj.toString());
            }
            put(key, obj);
        });
        matchData.addAll(invokeResult);
        return matchData;
    }

    @Override
    public Map multiGetToMap(List keys, CacheInvoker vInvoker) {
        Map matchData = new HashMap();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            ValueWrapper valueWrapper = super.get(key);
            if (valueWrapper != null) {
                matchData.put(key, valueWrapper.get());
                iterator.remove();
            }
        }

        if (keys.isEmpty()) {
            return matchData;
        }
        Map invokeResult = (Map) vInvoker.invoke();
        invokeResult.forEach((k, v) -> {
            put(k, v);
            matchData.put(k, v);
        });
        return matchData;
    }

    @Override
    public Object evict(Object key, CacheInvoker vInvoker) {
        super.evict(key);
        return vInvoker.invoke();
    }

    @Override
    public Object multiEvict(List keys, CacheInvoker vInvoker) {
        if (keys != null) {
            keys.stream().forEach(super::evict);
        }
        return vInvoker.invoke();
    }

}
