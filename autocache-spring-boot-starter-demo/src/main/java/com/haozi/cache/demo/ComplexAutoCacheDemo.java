package com.haozi.cache.demo;

import com.haozi.cache.core.AutoCache;
import com.haozi.cache.core.AutoCacheField;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 缓存使用（复杂）
 *
 * @author haozi
 */
@Component
public class ComplexAutoCacheDemo {

    /**
     * 缓存元素
     * <p>
     * 返回结果为集合时，可以缓存内部的元素
     *
     * @param ids ids
     * @return list
     */
    @AutoCache(remoteTTL = 30, elementCache = true)
    public List<ElementDemo> batchCache(List<Long> ids) {
        return Arrays.asList(new ElementDemo("abc"), new ElementDemo("def"));
    }

    @AllArgsConstructor
    static class ElementDemo {

        @AutoCacheField
        private String name;
    }
}
