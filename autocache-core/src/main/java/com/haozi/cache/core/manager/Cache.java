package com.haozi.cache.core.manager;

import com.haozi.cache.core.interceptor.CacheInvoker;

import java.util.List;
import java.util.Map;

/**
 * 缓存接口
 *
 * @author haozi
 * @date 2020-02-1910:57
 */
public interface Cache<K, V> {
    /**
     * 单个key获取value值
     *
     * @param key
     * @param vInvoker
     * @return
     */
    V get(K key, CacheInvoker vInvoker);

    /**
     * 获取批量结果
     *
     * @param keys
     * @return
     */
    List<V> multiGet(List<K> keys, CacheInvoker vInvoker);

    /**
     * 获取批量结果
     *
     * @param keys
     * @return
     */
    Map<K, V> multiGetToMap(List<K> keys, CacheInvoker vInvoker);

    /**
     * 单个删除
     *
     * @param key
     * @param vInvoker
     * @return
     */
    Object evict(K key, CacheInvoker vInvoker);

    /**
     * 批量删除
     *
     * @param keys
     * @param vInvoker
     * @return
     */
    Object multiEvict(List<K> keys, CacheInvoker vInvoker);
}
