package org.ying.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 存储键值对到Redis
    public void setKey(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 存储键值对到Redis
    public void setKey(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
    }

    // 存储键值对到Redis
    public void setKey(String key, Object value, long time, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, time, unit);
    }

    // 从Redis获取指定键的值
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 删除Redis中的指定键
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
}