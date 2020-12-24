package com.haozi.cache.core.configuration;

import com.haozi.cache.core.interceptor.AutoCacheInterceptor;
import com.haozi.cache.core.interceptor.CacheOperationSourceAdvisor;
import com.haozi.cache.core.manager.CacheFactory;
import com.haozi.cache.core.manager.AutoCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * 自动缓存bean配置
 *
 * @author haozi
 */
@Configuration
public class AutoCacheConfiguration {

    @Bean
    public RedisCacheWriter redisCacheWriter(RedisConnectionFactory connectionFactory) {
        return RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
    }

    @Bean
    public CacheFactory cacheFactory(RedisCacheWriter redisCacheWriter) {
        return new CacheFactory(redisCacheWriter);
    }

    @Bean
    public AutoCacheManager autoCacheManager(CacheFactory cacheFactory) {
        AutoCacheManager cacheManager = new AutoCacheManager(cacheFactory);
        return cacheManager;
    }

    @Bean
    public AutoCacheInterceptor autoCacheInterceptor(AutoCacheManager cacheManager) {
        AutoCacheInterceptor interceptor = new AutoCacheInterceptor(cacheManager);
        return interceptor;
    }

    @Bean
    public CacheOperationSourceAdvisor cacheAdvisor(AutoCacheInterceptor cacheInterceptor) {
        CacheOperationSourceAdvisor CacheOperationSourceAdvisor = new CacheOperationSourceAdvisor();
        CacheOperationSourceAdvisor.setAdvice(cacheInterceptor);
        return CacheOperationSourceAdvisor;
    }
}
