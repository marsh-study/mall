package com.mall.backend.service.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class BlacklistService {
    private static final String BLACKLIST_PREFIX = "blacklist::";

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void addToBlacklist(String jti, long ttlMillis) {
        if (ttlMillis > 0) {
            redisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + jti, 
                "", 
                ttlMillis, 
                TimeUnit.MILLISECONDS
            );
        }
    }

    public boolean isInBlacklist(String jti) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + jti);
    }

    public void removeFromBlacklist(String jti) {
        redisTemplate.delete(BLACKLIST_PREFIX + jti);
    }
}
