package org.shirakawatyu.handixikebackend.config;

import com.alibaba.fastjson2.support.spring6.data.redis.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching
@Configuration
public class CacheConfig {
    @Value("${cache.default.expire-time}")
    private long expireTime;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(expireTime))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer))
                .disableCachingNullValues();
        return RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .cacheDefaults(cacheConfig)
                // 配置同步修改或删除 put/evict
                .transactionAware()
                .build();
    }
}
