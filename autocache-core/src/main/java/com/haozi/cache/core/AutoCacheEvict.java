package com.haozi.cache.core;

import java.lang.annotation.*;

/**
 * 缓存删除
 * <p>
 * 不建议在本地缓存，使用缓存删除；应用多节点，无用！
 *
 * @author haozi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Repeatable(value = AutoCacheEvicts.class)
public @interface AutoCacheEvict {
    /**
     * 删除缓存名
     *
     * @return cacheName
     */
    String cacheName();

    /**
     * 自定义key
     * <p>
     * 支持Spring Expression Language (SpEL)解析表达式
     *
     * @return key
     */
    String key() default "";
}
