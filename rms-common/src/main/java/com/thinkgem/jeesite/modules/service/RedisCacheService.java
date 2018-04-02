package com.thinkgem.jeesite.modules.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private FastJsonRedisTemplate fastJsonRedisTemplate;

    public void setFastJsonRedisTemplate(FastJsonRedisTemplate fastJsonRedisTemplate) {
        this.fastJsonRedisTemplate = fastJsonRedisTemplate;
    }

    public boolean exists(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public boolean existsHashKey(String key, String hashKey) {
        return stringRedisTemplate.opsForHash().hasKey(key, hashKey);
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void setString(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void setString(String key, String value, long expiredMilliSec) {
        stringRedisTemplate.opsForValue().set(key, value, expiredMilliSec);
    }

    public <T> T getObject(String key, Class<T> clazz) throws Exception {
        return fastJsonRedisTemplate.getValue(key, clazz);
    }

    public void setObject(String key, Object obj) throws Exception {
        fastJsonRedisTemplate.setValue(key, obj);
    }

    public void setObject(String key, Object obj, long expiredMilliSec) throws Exception {
        fastJsonRedisTemplate.setValue(key, obj, expiredMilliSec);
    }

    public void setObject(String key, Object obj, long timeout, TimeUnit timeUnit) throws Exception {
        fastJsonRedisTemplate.setValue(key, obj, timeout, timeUnit);
    }

    ;

    public void setHash(String key, String hashKey, Object obj) throws Exception {
        fastJsonRedisTemplate.setHash(key, hashKey, obj);
    }

    public void setHash(String key, String hashKey, Object obj, long offset, TimeUnit timeUnit) throws Exception {
        fastJsonRedisTemplate.setHash(key, hashKey, obj, offset, timeUnit);
    }

    public <T> T getHash(String key, String hashKey, Class<T> clazz) throws Exception {
        return fastJsonRedisTemplate.getHash(key, hashKey, clazz);
    }

    public <T> Map<String, T> getEntireHash(String key, Class<T> clazz) throws Exception {
        return fastJsonRedisTemplate.getEntireHash(key, clazz);
    }

    public Map<String, String> getEntireHashString(String key) throws Exception {
        HashOperations<String, String, String> hashOper = stringRedisTemplate.opsForHash();
        Map<String, String> value = hashOper.entries(key);
        return value;
    }

    public <T> List<T> getHashArray(String key, String hashKey, Class<T> clazz) throws Exception {
        return fastJsonRedisTemplate.getHashArray(key, hashKey, clazz);
    }

    public void setHashString(String key, String hashKey, String value, long timeout, TimeUnit timeUnit) throws Exception {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
        if (timeout > 0) {
            stringRedisTemplate.expire(key, timeout, timeUnit);
        }
    }

    public String getHashString(String key, String hashKey) throws Exception {
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        return opsForHash.get(key, hashKey);
    }

    public void setZset(String key, Object obj, double score, long timeout, TimeUnit timeUnit) throws Exception {
        fastJsonRedisTemplate.setZSet(key, obj, score, timeout, timeUnit);
    }

    public <T> List<T> getZSet(String key, double min, double max, Class<T> clazz) throws Exception {
        return fastJsonRedisTemplate.getZSet(key, min, max, clazz);
    }

    public void setZsetString(String key, String value, double score, long timeout, TimeUnit timeUnit) throws Exception {
        stringRedisTemplate.opsForZSet().add(key, value, score);
        if (timeout > 0) {
            stringRedisTemplate.expire(key, timeout, timeUnit);
        }
    }

    public Set<String> getZSetString(String key, double min, double max) throws Exception {
        ZSetOperations<String, String> opsForZSet = stringRedisTemplate.opsForZSet();
        return opsForZSet.rangeByScore(key, min, max);
    }

    public <T> List<T> getArray(String key, Class<T> clazz) throws Exception {
        return fastJsonRedisTemplate.getValueArray(key, clazz);
    }

    public void removeZSetRangeByScore(String key, double min, double max) {
        ZSetOperations<String, String> opsForZSet = stringRedisTemplate.opsForZSet();
        opsForZSet.removeRangeByScore(key, min, max);
    }
}
