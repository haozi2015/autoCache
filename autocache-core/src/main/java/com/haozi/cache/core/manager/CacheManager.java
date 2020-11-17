package com.haozi.cache.core.manager;

import com.haozi.cache.core.interceptor.CacheOperation;
import com.haozi.cache.core.interceptor.CacheOperationContext;
import com.haozi.cache.core.util.CacheUtil;

/**
 * 缓存管理中心
 *
 * @author haozi
 */
public class CacheManager {
    private CacheFactory cacheFactory;

    public CacheManager(CacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
    }

    public Object execute(CacheOperationContext cacheOperationContext) {
        CacheOperation operation = cacheOperationContext.getOperation();
        Cache cache = cacheFactory.getCache(operation);
        if (cache == null) {
            return cacheOperationContext.getInvoker().invoke();
        }
        if (operation.isDefinitionKey()) {
            return executeKey(cacheOperationContext, cache);
        } else {
            return executeField(cacheOperationContext, cache);
        }
    }

    private Object executeKey(CacheOperationContext cacheOperationContext, Cache cache) {
        CacheOperation operation = cacheOperationContext.getOperation();
        Object key = CacheUtil.generateKey(operation.getKeySEL(), cacheOperationContext.getMethod(), cacheOperationContext.getArgs());
        // 删除
        if (cacheOperationContext.getOperation().isEvict()) {
            return cache.evict(key, cacheOperationContext.getInvoker());
        }
        return cache.get(key, cacheOperationContext.getInvoker());
    }

    private Object executeField(CacheOperationContext cacheOperationContext, Cache cache) {
        if (cacheOperationContext.isArgsList()) {
            // 批量删除
            if (cacheOperationContext.getOperation().isEvict()) {
                return cache.multiEvict(cacheOperationContext.getArgs0List(), cacheOperationContext.getInvoker());
            }
            // 返回集合List
            if (cacheOperationContext.isCacheFieldList()) {
                return cache.multiGet(cacheOperationContext.getArgs0List(), cacheOperationContext.getInvoker());
            }
            // 返回集合Map
            if (cacheOperationContext.isCacheFieldMap()) {
                return cache.multiGetToMap(cacheOperationContext.getArgs0List(), cacheOperationContext.getInvoker());
            }
        }
        Object key = CacheUtil.generateDefaultKey(cacheOperationContext.getArgs());
        // 删除
        if (cacheOperationContext.getOperation().isEvict()) {
            return cache.evict(key, cacheOperationContext.getInvoker());
        }
        return cache.get(key, cacheOperationContext.getInvoker());
    }
}
