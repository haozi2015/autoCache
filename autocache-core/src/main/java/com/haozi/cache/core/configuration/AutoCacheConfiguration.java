package com.haozi.cache.core.configuration;

import com.haozi.cache.core.interceptor.AutoCacheInterceptor;
import com.haozi.cache.core.interceptor.CacheOperationSourceAdvisor;
import com.haozi.cache.core.manager.CacheFactory;
import com.haozi.cache.core.manager.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * 自动缓存bean配置
 *
 * @author haozi
 * @date 2020/10/1911:06 上午
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
    public CacheManager cacheManager(CacheFactory cacheFactory) {
        CacheManager cacheManager = new CacheManager(cacheFactory);
        return cacheManager;
    }

    @Bean
    public AutoCacheInterceptor cacheInterceptor(CacheManager cacheManager) {
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
