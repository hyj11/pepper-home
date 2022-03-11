package com.sim.landlord.service;

import com.alibaba.fastjson.JSONObject;
import com.sim.landlord.cons.RedisCons;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @Author: Huang Yujiao
 * @Date: 2021/9/30 11:22
 * @Desc:
 */
@Service
public class LoginService {

    @Resource
    RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 存储登录人信息
     */
    public void login(String key, String userName) {
        if (StringUtils.isEmpty(key)) {
            key = "1";
        }
        Serializable s = redisTemplate.opsForValue().get(RedisCons.LANDLORDS_LOGIN_KEY + key);
        JSONObject js = null;
        if (s == null) {
            js = JSONObject.parseObject(userName);
        } else {
            js = (JSONObject) s;
            js.put(userName, userName);
        }
        redisTemplate.opsForValue().set(RedisCons.LANDLORDS_LOGIN_KEY + key, js.toJSONString());
    }

    /**
     * 退出房间
     */
    public void unlogin(String key) {
        redisTemplate.delete(RedisCons.LANDLORDS_LOGIN_KEY + key);
    }

    /**
     * 获取当前房间所有人
     */
    public void getByKey(String key) {
        if (StringUtils.isEmpty(key)) {
            key = "1";
        }
        redisTemplate.opsForValue().get(RedisCons.LANDLORDS_LOGIN_KEY + key);
    }
}
