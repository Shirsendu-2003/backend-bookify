package com.bookingplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    // ==========================================
    // SAVE WITHOUT TTL
    // ==========================================

    public void save(
            String key,
            Object value
    ) {

        try {

            redisTemplate.opsForValue()
                    .set(key, value);

            log.info(
                    "Redis key saved: {}",
                    key
            );

        } catch (Exception ex) {

            log.error(
                    "Redis save failed for key: {}",
                    key,
                    ex
            );

            throw new RuntimeException(
                    "Redis save operation failed"
            );
        }
    }

    // ==========================================
    // SAVE WITH TTL (SECONDS)
    // ==========================================

    public void save(
            String key,
            Object value,
            long ttlSeconds
    ) {

        try {

            redisTemplate.opsForValue()
                    .set(
                            key,
                            value,
                            ttlSeconds,
                            TimeUnit.SECONDS
                    );

            log.info(
                    "Redis key saved with TTL: {} | {} sec",
                    key,
                    ttlSeconds
            );

        } catch (Exception ex) {

            log.error(
                    "Redis TTL save failed for key: {}",
                    key,
                    ex
            );

            throw new RuntimeException(
                    "Redis TTL save failed"
            );
        }
    }

    // ==========================================
    // SAVE WITH DURATION
    // ==========================================

    public void save(
            String key,
            Object value,
            Duration duration
    ) {

        try {

            redisTemplate.opsForValue()
                    .set(
                            key,
                            value,
                            duration
                    );

            log.info(
                    "Redis key saved with duration: {}",
                    key
            );

        } catch (Exception ex) {

            log.error(
                    "Redis duration save failed",
                    ex
            );

            throw new RuntimeException(
                    "Redis duration save failed"
            );
        }
    }

    // ==========================================
    // GET VALUE
    // ==========================================

    public Object get(
            String key
    ) {

        try {

            Object value =
                    redisTemplate.opsForValue()
                            .get(key);

            log.info(
                    "Redis key fetched: {}",
                    key
            );

            return value;

        } catch (Exception ex) {

            log.error(
                    "Redis get failed for key: {}",
                    key,
                    ex
            );

            throw new RuntimeException(
                    "Redis get failed"
            );
        }
    }

    // ==========================================
    // GENERIC GET
    // ==========================================

    @SuppressWarnings("unchecked")
    public <T> T get(
            String key,
            Class<T> clazz
    ) {

        Object value = get(key);

        if (value == null) {
            return null;
        }

        return (T) value;
    }

    // ==========================================
    // DELETE KEY
    // ==========================================

    public boolean delete(
            String key
    ) {

        try {

            Boolean deleted =
                    redisTemplate.delete(key);

            log.info(
                    "Redis key deleted: {}",
                    key
            );

            return Boolean.TRUE.equals(deleted);

        } catch (Exception ex) {

            log.error(
                    "Redis delete failed: {}",
                    key,
                    ex
            );

            throw new RuntimeException(
                    "Redis delete failed"
            );
        }
    }

    // ==========================================
    // EXISTS CHECK
    // ==========================================

    public boolean exists(
            String key
    ) {

        try {

            Boolean exists =
                    redisTemplate.hasKey(key);

            return Boolean.TRUE.equals(exists);

        } catch (Exception ex) {

            log.error(
                    "Redis exists check failed",
                    ex
            );

            throw new RuntimeException(
                    "Redis exists check failed"
            );
        }
    }

    // ==========================================
    // SET TTL
    // ==========================================

    public boolean expire(
            String key,
            long ttlSeconds
    ) {

        try {

            Boolean result =
                    redisTemplate.expire(
                            key,
                            ttlSeconds,
                            TimeUnit.SECONDS
                    );

            log.info(
                    "TTL applied: {} sec for key {}",
                    ttlSeconds,
                    key
            );

            return Boolean.TRUE.equals(result);

        } catch (Exception ex) {

            log.error(
                    "Redis expire failed",
                    ex
            );

            throw new RuntimeException(
                    "Redis expire failed"
            );
        }
    }

    // ==========================================
    // HASH PUT
    // ==========================================

    public void putHash(
            String key,
            String field,
            Object value
    ) {

        try {

            redisTemplate.opsForHash()
                    .put(
                            key,
                            field,
                            value
                    );

            log.info(
                    "Redis hash saved: {}:{}",
                    key,
                    field
            );

        } catch (Exception ex) {

            log.error(
                    "Redis hash put failed",
                    ex
            );

            throw new RuntimeException(
                    "Redis hash put failed"
            );
        }
    }

    // ==========================================
    // HASH GET
    // ==========================================

    public Object getHash(
            String key,
            String field
    ) {

        try {

            return redisTemplate.opsForHash()
                    .get(
                            key,
                            field
                    );

        } catch (Exception ex) {

            log.error(
                    "Redis hash get failed",
                    ex
            );

            throw new RuntimeException(
                    "Redis hash get failed"
            );
        }
    }

    // ==========================================
    // HASH GET ALL
    // ==========================================

    public Map<Object, Object> getAllHash(
            String key
    ) {

        try {

            return redisTemplate.opsForHash()
                    .entries(key);

        } catch (Exception ex) {

            log.error(
                    "Redis hash entries failed",
                    ex
            );

            throw new RuntimeException(
                    "Redis hash fetch failed"
            );
        }
    }

    // ==========================================
    // HASH DELETE
    // ==========================================

    public void deleteHashField(
            String key,
            String field
    ) {

        try {

            redisTemplate.opsForHash()
                    .delete(
                            key,
                            field
                    );

            log.info(
                    "Redis hash field deleted: {}:{}",
                    key,
                    field
            );

        } catch (Exception ex) {

            log.error(
                    "Redis hash delete failed",
                    ex
            );

            throw new RuntimeException(
                    "Redis hash delete failed"
            );
        }
    }

    // ==========================================
    // CLEAR ALL CACHE
    // ==========================================

    public void clearAll() {

        try {

            Set<String> keys =
                    redisTemplate.keys("*");

            if (keys != null &&
                    !keys.isEmpty()) {

                redisTemplate.delete(keys);

                log.warn(
                        "All Redis cache cleared"
                );
            }

        } catch (Exception ex) {

            log.error(
                    "Redis clearAll failed",
                    ex
            );

            throw new RuntimeException(
                    "Redis cache clear failed"
            );
        }
    }

}