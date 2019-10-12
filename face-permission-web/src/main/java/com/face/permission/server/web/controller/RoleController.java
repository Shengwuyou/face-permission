package com.face.permission.server.web.controller;

import com.face.permission.common.annoations.LoginIntercept;
import com.face.permission.common.responses.ResultInfo;
import com.face.permission.mapper.domain.PRoleDo;
import com.face.permission.service.interfaces.roles.IRoleService;
import com.face.permission.service.template.RedisSelfCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-05 17:35
 */
@Controller
@RequestMapping(value = "role")
public class RoleController {

    @Autowired
    RedisSelfCacheManager redisCacheManager;

    @Autowired
    IRoleService roleService;

    @RequestMapping(method = RequestMethod.GET, value = "/getRoles")
    @ResponseBody
    @LoginIntercept(require = false)
    public ResultInfo<?> getRoles(){
        List<PRoleDo> roleDos = roleService.getAllCanSetRoles();
        return ResultInfo.success(roleDos);
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
