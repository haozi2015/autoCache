package com.haozi.cache.core.configuration;

import com.haozi.cache.core.interceptor.AutoCacheInterceptor;
import com.haozi.cache.core.interceptor.CacheOperationSourceAdvisor;
import com.haozi.cache.core.manager.AutoCacheManager;
import com.haozi.cache.core.manager.CacheFactory;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("#{!'${spring.redis.host:}'.equals('') || !'${spring.redis.cluster.nodes:}'.equals('')}")
    private Boolean containRemoteCache;

    @Bean
    public CacheFactory cacheFactory(RedisConnectionFactory connectionFactory) {
        return new CacheFactory(containRemoteCache ? RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory) : null);
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
