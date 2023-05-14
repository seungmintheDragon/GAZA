package com.idle.gaza.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Component
public class RedisUtil {

    private static RedisTemplate<String,String> redisTemplate;

    @Autowired
    public RedisUtil (RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static String getData(String key){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public static void setData(String key, String value){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key,value);
    }

    public static void setDataExpire(String key,String value,long duration){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key,value,expireDuration);
    }

    public static void deleteData(String key){
        redisTemplate.delete(key);
    }

}