package com.haozi.cache.core.interceptor;

import com.haozi.cache.core.AutoCache;
import com.haozi.cache.core.AutoCacheEvict;
import com.haozi.cache.core.AutoCacheEvicts;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 匹配自定义注解
 *
 * @author haozi
 */
public class CacheOperationSourcePointcut extends StaticMethodMatcherPointcut {
    private final Map<Object, Boolean> attributeCache = new ConcurrentHashMap<>(1024);
    private final List<Class<? extends Annotation>> matchesList = Arrays.asList(AutoCache.class, AutoCacheEvict.class, AutoCacheEvicts.class);

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        Object key = getCacheKey(method, aClass);
        Boolean flag = attributeCache.get(key);
        if (flag != null) {
            return flag;
        }
        flag = matchesList.stream().anyMatch(an -> AnnotationUtils.findAnnotation(method, an) != null);
        attributeCache.put(key, flag);
        return flag;
    }

    protected Object getCacheKey(Method method, @Nullable Class<?> targetClass) {
        return new MethodClassKey(method, targetClass);
    }
}
