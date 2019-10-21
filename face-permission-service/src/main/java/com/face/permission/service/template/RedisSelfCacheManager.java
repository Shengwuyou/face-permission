package com.face.permission.service.template;

import com.alibaba.fastjson.JSONObject;
import com.face.permission.common.utils.LoggerUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-20 10:59
 */
@Component
public class RedisSelfCacheManager {

    private Logger logger = LoggerUtil.COMMON_DEFAULT;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 插入redis数据，默认超时60秒
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setIfAbsent(String key, String value, int second) {
        if (second <= 0) {
            second = 60;
        }
        return setIfAbsent(key, value, second * 1000);
    }

    /**
     * @param key
     * @param value
     * @param expireTime
     */
    public boolean setIfAbsent(String key, String value, long expireTime) {
        try {
            Duration duration = Duration.ofMillis(expireTime);
            return redisTemplate.opsForValue().setIfAbsent(key, value, duration);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param key
     * @param value
     * @param seconds
     */
    public void set(String key, String value, long seconds) {
        try {
            Duration duration = Duration.ofSeconds(seconds);
            redisTemplate.opsForValue().set(key, value, duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @param key
     * @return
     */
    public <T> T get(String key, Class<T> className) {
        try {
            Object obj = key == null ? null : redisTemplate.opsForValue().get(key);
            if (obj != null){
                return JSONObject.parseObject((String) obj, className);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param key
     * @return
     */
    public <T> List<T> getList(String key, Class<T> className) {
        try {
            Object obj = key == null ? null : redisTemplate.opsForValue().get(key);
            if (obj != null){
                return JSONObject.parseArray((String) obj, className);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param key
     * @return
     */
    public Object get(String key) {
        try {
            return key == null ? null : redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除redis中存在的数据
     * @param key
     * @return
     */
    public boolean remove(String key){
        return redisTemplate.delete(key);
    }

    /**
     * 删除redis中存在的数据
     * @param keys
     * @return
     */
    public boolean removeArr(List<String> keys){
        try {
            Long successNum = redisTemplate.delete(keys);
            if (successNum != keys.size()){
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 加锁
     *
     * @param key
     * @param seconds
     * @return
     */
    public boolean lock(String key, Long seconds) {
        try {
            if (key == null) {
                return false;
            } else if (seconds <= 0) {
                seconds = 60L;
            }
            return redisTemplate.opsForValue().setIfAbsent(key, 1, seconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 加锁
     *
     * @param key
     * @return
     */
    public void unLock(String key) {
        try {
            if (key != null && redisTemplate.hasKey(key)) {
                redisTemplate.delete(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void rightPush(String key, String value) {
        if (StringUtils.isAnyBlank(key, value)) {
            return;
        }
        try {
            redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            LoggerUtil.error(logger, e, "redis异常");
        }
    }

    public void rightPushAll(String key, List<String> values) {
        if (Objects.isNull(key) || CollectionUtils.isEmpty(values)) {
            return;
        }
        try {
            redisTemplate.opsForList().rightPushAll(key, values);
        } catch (Exception e) {
            LoggerUtil.error(logger, e, "redis异常");
        }
    }

    public Object leftPop(String key) {
        if (Objects.isNull(key)) {
            return null;
        }
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            LoggerUtil.error(logger, e, "redis异常");
            return null;
        }
    }

    public Object rightPop(String key) {
        if (Objects.isNull(key)) {
            return null;
        }
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            LoggerUtil.error(logger, e, "redis异常");
            return null;
        }
    }

    public Long listSize(String key) {
        if (Objects.isNull(key)) {
            return 0L;
        }
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            LoggerUtil.error(logger, e, "redis异常");
            return 0L;
        }
    }

    public List<Object> range(String key, Integer start, Integer end) {
        if (Objects.isNull(key)) {
            return null;
        }
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            LoggerUtil.error(logger, e, "redis异常");
            return null;
        }
    }

}
