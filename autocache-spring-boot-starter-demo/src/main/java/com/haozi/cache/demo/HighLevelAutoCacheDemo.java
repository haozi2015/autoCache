package com.haozi.cache.demo;

import com.haozi.cache.core.AutoCache;
import com.haozi.cache.core.AutoCacheEvict;
import org.springframework.stereotype.Component;

/**
 * 缓存使用（高级）
 *
 * @author zhanghao
 * @date 2021/2/184:31 下午
 */
@Component
public class HighLevelAutoCacheDemo {
    /**
     * 自定义缓存key规则
     * <p>
     * 可用在共享缓存结果，非autoCache使用
     *
     * @return
     */
    @AutoCache(remoteTTL = 30, key = "#id + ':' + #name")
    public String customKey(String id, String name) {
        return "abc";
    }

    /**
     * 主动缓存清除
     * <p>
     * cacheName保持一致
     *
     * @param id
     */
    @AutoCacheEvict(cacheName = "evict_demo")
    public void evictStr(String id) {
    }

    @AutoCache(remoteTTL = 30, cacheName = "evict_demo")
    public String getStr(String id) {
        return "abc";
    }


}
