package com.haozi.cache.core.interceptor;

import com.haozi.cache.core.AutoCache;
import com.haozi.cache.core.AutoCacheEvict;
import com.haozi.cache.core.util.CacheUtil;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 缓存定义
 *
 * @author haozi
 * @date 2020/10/195:50 下午
 */
@Data
@Builder
public class CacheOperation {
    private String keySEL;
    private Long localCacheExpire;
    private Long remoteCacheExpire;
    private boolean elementCache;
    private String cacheName;
    // 删除标识 true
    private boolean evict;

    /**
     * 是否定义key
     *
     * @return
     */
    public boolean isDefinitionKey() {
        return keySEL != null && !"".equals(keySEL);
    }


    public static CacheOperation build(AutoCacheEvict autoCacheEvict) {
        String cacheName = autoCacheEvict.cacheName();
        if (StringUtils.isEmpty(cacheName)) {
            throw new IllegalArgumentException("AutoCacheEvict[cacheName] must be not blank");
        }
        return CacheOperation.builder()
                .keySEL(autoCacheEvict.key())
                .cacheName(cacheName)
                .evict(true)
                .build();
    }

    public static CacheOperation build(AutoCache autoCache, Method method) {
        if (autoCache.localTTL() <= 0L && autoCache.remoteTTL() <= 0L) {
            throw new IllegalArgumentException("AutoCache [localTTL] > 0 or [remoteTTL] > 0");
        }
        return CacheOperation.builder()
                .localCacheExpire(autoCache.localTTL() <= 0L ? null : autoCache.localTTL())
                .remoteCacheExpire(autoCache.remoteTTL() <= 0L ? null : autoCache.remoteTTL())
                .keySEL(autoCache.key())
                .elementCache(autoCache.elementCache())
                .cacheName(StringUtils.isEmpty(autoCache.cacheName()) ? CacheUtil.getDefaultCacheName(method) : autoCache.cacheName())
                .build();
    }

}
