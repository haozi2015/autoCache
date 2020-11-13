package com.haozi.cache.core.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.haozi.cache.core.interceptor.CacheInvoker;
import com.haozi.cache.core.util.CacheUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存
 *
 * @author haozi
 * @date 2020/10/2210:51 上午
 */
@Slf4j
public class LocalCache extends AbstractCache {

    private Cache cache;

    public LocalCache(long ttl) {
        cache = Caffeine.newBuilder()
                //最后一次写入后，expire单位时间后过期
                .expireAfterWrite(ttl, TimeUnit.SECONDS)
                .build();
    }

    @Override
    void _put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    void _evict(List keys) {
        cache.invalidateAll(keys);
    }

    @Override
    Object _get(Object key, CacheInvoker vInvoker) {
        return cache.get(key, (k) -> vInvoker.invoke());
    }

    @Override
    List _multiGet(List keys, CacheInvoker vInvoker) {
        Map dataMap = cache.getAllPresent(keys);
        keys.removeAll(dataMap.keySet());
        if (keys.isEmpty()) {
            return new ArrayList(dataMap.values());
        }
        List list = (List) vInvoker.invoke();
        list.forEach(obj -> {
            Object key = CacheUtil.getCacheFieldValue(obj);
            if (key == null) {
                throw new IllegalArgumentException("key is null. class " + obj.getClass() + " data " + obj.toString());
            }
            _put(key, obj);
        });
        list.addAll(dataMap.values());
        return list;
    }

    @Override
    Map _multiGetToMap(List keys, CacheInvoker vInvoker) {
        Map dataMap = cache.getAllPresent(keys);
        keys.removeAll(dataMap.keySet());
        if (keys.isEmpty()) {
            return dataMap;
        }
        Map map = (Map) vInvoker.invoke();
        map.forEach((k, v) -> {
            _put(k, v);
        });
        map.putAll(dataMap);
        return map;
    }
}
