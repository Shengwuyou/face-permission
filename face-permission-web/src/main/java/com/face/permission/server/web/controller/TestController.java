package com.face.permission.server.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.face.permission.common.utils.IpUtil;
import com.face.permission.server.config.annoations.LoginIntercept;
import com.face.permission.service.interfaces.test.ITestService;
import com.face.permission.service.template.RedisSelfCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-05 17:35
 */
@Controller
public class TestController {

    @Autowired
    ITestService testService;

    @Autowired
    RedisSelfCacheManager redisCacheManager;

    @RequestMapping(method = RequestMethod.GET, value = "/test")
    @ResponseBody
    @LoginIntercept(require = false)
    public String getIp(HttpServletRequest request){
        testService.test();
        JSONObject rt = new JSONObject();
        rt.put("serverIP", IpUtil.getIpAddress());
        rt.put("requestIP", IpUtil.getIpAddress(request));
        return rt.toJSONString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/redis")
    @ResponseBody
    @LoginIntercept(require = false)
    public boolean redisCache(String time){
        long times = System.currentTimeMillis();
        return redisCacheManager.setIfAbsent(time, "时间戳：" + times, 60*1000);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/redis/get")
    @ResponseBody
    @LoginIntercept(require = false)
    public Object getRedisChche(String time){
        return redisCacheManager.get(time, String.class);
    }
}
