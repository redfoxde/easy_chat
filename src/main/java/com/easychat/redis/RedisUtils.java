package com.easychat.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // ------------------------- 通用操作 -------------------------

    /**
     * 设置过期时间
     * @param key   键
     * @param time  时间
     * @param unit  时间单位
     */
    public boolean expire(String key, long time, TimeUnit unit) {
        return redisTemplate.expire(key, time, unit);
    }

    /**
     * 判断 key 是否存在
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除 key
     */
    public boolean delete(String key) {
        Boolean success = redisTemplate.delete(key);
        return success != null && success;
    }

    // ------------------------- String 类型操作 -------------------------

    /**
     * 存储对象（自动序列化）
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long time) {
        set(key, value, time, TimeUnit.SECONDS); // 默认单位设为秒
    }

    /**
     * 存储对象并设置过期时间
     */
    public void set(String key, Object value, long time, TimeUnit unit) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, unit);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 获取对象（自动反序列化）
     */
    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 安全获取对象（处理 JSON 反序列化）
     */
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) return null;
        // 如果存储的是 JSON 字符串，手动反序列化
        if (value instanceof String) {
            return JSON.parseObject((String) value, clazz);
        }
        // 直接返回对象（需确保 RedisTemplate 配置了正确的序列化器）
        return clazz.cast(value);
    }

    // ------------------------- Hash 类型操作 -------------------------

    /**
     * 存储 Hash 数据
     */
    public void putHashAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 获取 Hash 字段值
     */
    public <T> T getHash(String key, String hashKey, Class<T> clazz) {
        Object value = redisTemplate.opsForHash().get(key, hashKey);
        return clazz.isInstance(value) ? clazz.cast(value) : null;
    }

    // ------------------------- List 类型操作 -------------------------

    /**
     * 批量左推数据到列表
     */
    public Long leftPushAll(String key, List<?> values) {
        return redisTemplate.opsForList().leftPushAll(key, values.toArray());
    }

    /**
     * 获取列表范围数据（带类型转换）
     */
    public <T> List<T> range(String key, long start, long end, Class<T> clazz) {
        List<Object> values = redisTemplate.opsForList().range(key, start, end);
        return values.stream()
                .filter(clazz::isInstance)
                .map(obj -> clazz.cast(obj))
                .collect(Collectors.toList());
    }

    // ------------------------- Set 类型操作 -------------------------

    /**
     * 添加元素到集合
     */
    public Long addSet(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取集合所有元素（带类型转换）
     */
    public <T> Set<T> members(String key, Class<T> clazz) {
        Set<Object> values = redisTemplate.opsForSet().members(key);
        return values.stream()
                .filter(clazz::isInstance)
                .map(obj -> clazz.cast(obj))
                .collect(Collectors.toSet());
    }

    // ------------------------- 批量操作工具方法 -------------------------

    /**
     * 批量存储联系人列表（
     */
    public Long batchPushContacts(String key, List<String> contactIds, long expireSeconds) {
        String[] contactsArray = contactIds.toArray(new String[0]);
        Long size = redisTemplate.opsForList().leftPushAll(key, contactsArray);
        if (expireSeconds > 0) {
            redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
        }
        return size;
    }
}