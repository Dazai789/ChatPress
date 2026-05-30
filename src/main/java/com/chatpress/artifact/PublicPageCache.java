package com.chatpress.artifact;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class PublicPageCache {

    private static final String CACHE_NAME = "publicPage";

    private final CacheManager cacheManager;

    public PublicPageCache(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    private Cache cache() {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            throw new IllegalStateException("Cache '" + CACHE_NAME + "' not found");
        }
        return cache;
    }

    public String get(String slug) {
        Cache.ValueWrapper wrapper = cache().get(slug);
        return wrapper != null ? (String) wrapper.get() : null;
    }

    public void put(String slug, String html) {
        cache().put(slug, html);
    }

    public void evict(String slug) {
        cache().evict(slug);
    }
}
