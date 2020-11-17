package com.haozi.cache.core.manager;

import com.haozi.cache.core.interceptor.CacheInvoker;

import java.util.List;
import java.util.Map;

/**
 * 缓存接口
 *
 * @author haozi
 */
public interface Cache<K, V> {
    /**
     * 单个key获取value值
     *
     * @param key      key
     * @param vInvoker 执行器
     * @return 值
     */
    V get(K key, CacheInvoker vInvoker);

    /**
     * 获取批量结果
     *
     * @param keys     keys
     * @param vInvoker 执行器
     * @return 值
     */
    List<V> multiGet(List<K> keys, CacheInvoker vInvoker);

    /**
     * 获取批量结果
     *
     * @param keys     keys
     * @param vInvoker 执行器
     * @return 值
     */
    Map<K, V> multiGetToMap(List<K> keys, CacheInvoker vInvoker);

    /**
     * 单个删除
     *
     * @param key      keys
     * @param vInvoker 执行器
     * @return 值
     */
    Object evict(K key, CacheInvoker vInvoker);

    /**
     * 批量删除
     *
     * @param keys     keys
     * @param vInvoker 执行器
     * @return 值
     */
    Object multiEvict(List<K> keys, CacheInvoker vInvoker);
}
