package com.haozi.cache.core.manager;

import com.haozi.cache.core.interceptor.CacheInvoker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 缓存自定义共用约束
 *
 * @author haozi
 */
@Slf4j
public abstract class AbstractCache<K, V> implements Cache<K, V> {

    @Override
    public V get(K key, CacheInvoker vInvoker) {
        Assert.notNull(key, "key must not be null");
        return _get(key, vInvoker);
    }

    @Override
    public List<V> multiGet(List<K> keys, CacheInvoker vInvoker) {
        Assert.notEmpty(keys, "keys must not be empty");
        return _multiGet(keys, vInvoker);
    }

    @Override
    public Map<K, V> multiGetToMap(List<K> keys, CacheInvoker vInvoker) {
        Assert.notEmpty(keys, "keys must not be empty");
        return _multiGetToMap(keys, vInvoker);
    }

    @Override
    public Object evict(K key, CacheInvoker vInvoker) {
        _evict(Arrays.asList(key));
        return vInvoker.invoke();
    }

    @Override
    public Object multiEvict(List<K> keys, CacheInvoker vInvoker) {
        _evict(keys);
        return vInvoker.invoke();
    }

    /**
     * 缓存
     *
     * @param key
     * @param value
     */
    abstract void _put(K key, V value);

    /**
     * 删除
     *
     * @param keys
     */
    abstract void _evict(List<K> keys);

    /**
     * 单ID查询
     *
     * @param key
     * @return
     */
    abstract V _get(K key, CacheInvoker vInvoker);

    /**
     * 批量ID查询
     *
     * @param keys
     * @return 返回的List中不会含有NULL
     */
    abstract List<V> _multiGet(List<K> keys, CacheInvoker vInvoker);

    /**
     * 批量ID查询
     *
     * @param keys
     * @return 返回的Map中的Value可能为NULL
     */
    abstract Map<K, V> _multiGetToMap(List<K> keys, CacheInvoker vInvoker);
}
