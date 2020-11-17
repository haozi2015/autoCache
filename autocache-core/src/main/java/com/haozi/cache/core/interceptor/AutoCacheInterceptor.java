package com.haozi.cache.core.interceptor;

import com.haozi.cache.core.AutoCache;
import com.haozi.cache.core.AutoCacheEvict;
import com.haozi.cache.core.AutoCacheEvicts;
import com.haozi.cache.core.manager.CacheManager;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.interceptor.CacheOperationInvoker;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 自动缓存拦截器
 *
 * @author haozi
 */
@Slf4j
public class AutoCacheInterceptor implements MethodInterceptor, Serializable {

    private CacheManager cacheManager;

    public AutoCacheInterceptor(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        CacheInvoker aopAllianceInvoker = () -> {
            try {
                return invocation.proceed();
            } catch (Throwable ex) {
                throw new CacheInvoker.ThrowableWrapper(ex);
            }
        };

        try {
            return execute(aopAllianceInvoker, invocation.getThis(), invocation.getMethod(), invocation.getArguments());
        } catch (CacheOperationInvoker.ThrowableWrapper th) {
            throw th.getOriginal();
        }
    }

    private Object execute(CacheInvoker invoker, Object target, Method method, Object[] args) {
        CacheOperation cacheOperation;
        if ((cacheOperation = parseAutoCacheAnnotation(method)) != null
                || (cacheOperation = parseAutoCacheEvictAnnotation(method)) != null) {
            return cacheManager.execute(new CacheOperationContext(cacheOperation, target, method, args, invoker));
        }
        return invokeAutoCacheEvicts(invoker, target, method, args);
    }

    /**
     * 解析自动缓存注解
     *
     * @param method
     * @return
     */
    private CacheOperation parseAutoCacheAnnotation(Method method) {
        AutoCache autoCache = AnnotationUtils.findAnnotation(method, AutoCache.class);
        if (autoCache == null) {
            return null;
        }
        if (autoCache.localTTL() >= autoCache.remoteTTL() && autoCache.remoteTTL() > 0) {
            log.warn("method[" + method.getName() + "]不建议本地缓存时间大于远程缓存时间！！！");
        }
        return CacheOperation.build(autoCache, method);
    }

    /**
     * 解析缓存删除注解
     *
     * @param method
     * @return
     */
    private CacheOperation parseAutoCacheEvictAnnotation(Method method) {
        AutoCacheEvict autoCacheEvict = AnnotationUtils.findAnnotation(method, AutoCacheEvict.class);
        if (autoCacheEvict == null) {
            return null;
        }
        return CacheOperation.build(autoCacheEvict);
    }

    /**
     * 执行多删除注解
     *
     * @param invoker
     * @param target
     * @param method
     * @param args
     * @return
     */
    private Object invokeAutoCacheEvicts(CacheInvoker invoker, Object target, Method method, Object[] args) {
        AutoCacheEvicts autoCacheEvicts = AnnotationUtils.findAnnotation(method, AutoCacheEvicts.class);
        if (autoCacheEvicts == null) {
            return invoker.invoke();
        }

        AutoCacheEvict[] values = autoCacheEvicts.value();
        CacheInvoker lastCacheInvoker = invoker;
        CacheOperation lastCacheOperation = null;
        for (AutoCacheEvict autoCacheEvict : values) {
            lastCacheOperation = CacheOperation.build(autoCacheEvict);

            final CacheInvoker finalLastCacheInvoker = lastCacheInvoker;
            final CacheOperation finalLastCacheOperation = lastCacheOperation;
            lastCacheInvoker = () -> {
                try {
                    return cacheManager.execute(new CacheOperationContext(finalLastCacheOperation, target, method, args, finalLastCacheInvoker));
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    throw new CacheInvoker.ThrowableWrapper(ex);
                }
            };
        }
        return cacheManager.execute(new CacheOperationContext(lastCacheOperation, target, method, args, lastCacheInvoker));
    }
}
