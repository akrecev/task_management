package ru.kretsev.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for enabling and setting up caching.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Configures the cache manager with Caffeine settings.
     *
     * @return the {@link CacheManager} instance
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("tasks", "comments");
        cacheManager.setCaffeine(
                Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(100));
        return cacheManager;
    }
}
