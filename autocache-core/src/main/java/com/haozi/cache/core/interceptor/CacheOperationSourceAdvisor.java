package com.haozi.cache.core.interceptor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * 接入spring拦截器
 *
 * @author haozi
 * @date 2020/10/225:10 下午
 */
public class CacheOperationSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    private CacheOperationSourcePointcut mhCacheOperationSourcePointcut = new CacheOperationSourcePointcut();

    @Override
    public Pointcut getPointcut() {
        return mhCacheOperationSourcePointcut;
    }
}
