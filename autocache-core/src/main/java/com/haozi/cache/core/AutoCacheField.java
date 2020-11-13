package com.haozi.cache.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明对象中的字段为缓存key
 *
 * @author haozi
 * @date 2020/6/410:28 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AutoCacheField {

}
