package com.haozi.cache.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多重注解
 *
 * @author haozi
 * @date 2020/8/142:15 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AutoCacheEvicts {
    AutoCacheEvict[] value();
}
