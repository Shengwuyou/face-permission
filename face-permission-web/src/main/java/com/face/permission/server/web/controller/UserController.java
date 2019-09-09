package com.face.permission.server.web.controller;

import com.face.permission.api.model.request.user.UserInfo;
import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.api.model.request.valids.groups.CreateGroup;
import com.face.permission.api.model.request.valids.groups.RetrieveGroup;
import com.face.permission.api.model.request.valids.groups.UpdateGroup;
import com.face.permission.api.model.response.TokenDTO;
import com.face.permission.common.responses.PaginatedResultData;
import com.face.permission.common.responses.ResultInfo;
import com.face.permission.mapper.dto.request.UserLoginDTO;
import com.face.permission.mapper.query.user.UserQuery;
import com.face.permission.mapper.vo.user.UserInfoVo;
import com.face.permission.server.config.ThreadLocalUser;
import com.face.permission.server.config.annoations.LoginIntercept;
import com.face.permission.service.interfaces.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-18 14:38
 */
@Controller
@RequestMapping(value = "user")
@Validated
public class UserController {

    @Autowired
    IUserService userService;

    @RequestMapping(value = "selfRegister" ,method = RequestMethod.POST)
    @ResponseBody
    @LoginIntercept(require = false)
    //TODO AOP加操作日志
    public ResultInfo<?> selfRegister(@Validated(value = {CreateGroup.class}) @RequestBody UserRequest request){
        TokenDTO token = userService.selfRegister(request);
        return ResultInfo.success(token);
    }

    @RequestMapping(value = "cmsRegister" ,method = RequestMethod.POST)
    @ResponseBody
    @LoginIntercept
    //TODO AOP加操作日志
    public ResultInfo<?> cmsRegister(@Validated(value = {CreateGroup.class}) @RequestBody UserRequest request){
        UserInfo userInfo = ThreadLocalUser.getUserInfo();
        request.setUserInfo(userInfo);
        TokenDTO token = userService.cmsRegister(request);
        return ResultInfo.success(token);
    }


    @RequestMapping(value = "login" ,method = RequestMethod.POST)
    @ResponseBody
    @LoginIntercept(require = false)
    //TODO AOP加操作日志
    public ResultInfo<?> login(@Validated(value = {RetrieveGroup.class})  @RequestBody UserLoginDTO request){
        TokenDTO token = userService.login(request);
        return ResultInfo.success(token);
    }

    @RequestMapping(value = "update" ,method = RequestMethod.POST)
    @ResponseBody
    @LoginIntercept
    //TODO AOP加操作日志
    public ResultInfo<?> updateUserInfo(@Validated(value = {UpdateGroup.class})  @RequestBody UserRequest request){
        UserInfo userInfo = ThreadLocalUser.getUserInfo();
        request.setUserInfo(userInfo);
        Boolean result = userService.update(request);
        return ResultInfo.success(result);
    }

    @RequestMapping(value = "queryUsers" ,method = RequestMethod.POST)
    @ResponseBody
    //TODO AOP加操作日志
    public ResultInfo<PaginatedResultData<UserInfoVo>> queryUsers(@RequestBody UserQuery query){

        Integer total = userService.getTotal(query);
        List<UserInfoVo> resultList = new ArrayList();
        if (total > 0) {
            resultList = userService.getList(query);
        }
        return ResultInfo.buildPaginatedResult(query, resultList, total);
    }

    @RequestMapping(value = "delete/{userId}" ,method = RequestMethod.POST)
    @ResponseBody
    @LoginIntercept
    //TODO AOP加操作日志
    public ResultInfo<?> delete(@PathVariable("userId") String userId){
        return ResultInfo.success(userService.delete(ThreadLocalUser.getUserInfo() ,userId));
    }
}
