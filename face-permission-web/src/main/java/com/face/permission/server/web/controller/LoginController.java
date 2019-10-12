package com.face.permission.server.web.controller;

import com.face.permission.api.model.request.user.UserInfo;
import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.common.model.request.valids.groups.CreateGroup;
import com.face.permission.common.model.request.valids.groups.RetrieveGroup;
import com.face.permission.api.model.response.TokenDTO;
import com.face.permission.common.annoations.AopLog;
import com.face.permission.common.annoations.LoginIntercept;
import com.face.permission.common.annoations.RepeatSubmitCheck;
import com.face.permission.common.responses.ResultInfo;
import com.face.permission.mapper.dto.request.UserLoginDTO;
import com.face.permission.mapper.vo.user.UserInfoVo;
import com.face.permission.server.config.ThreadLocalUser;
import com.face.permission.service.interfaces.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-10-08 15:34
 */
@RestController
@RepeatSubmitCheck
public class LoginController {

    @Autowired
    IUserService userService;

    @PostMapping(value = "register/client")
    @AopLog(mehtodType = AopLog.MethodTypeEnum.ADD, mehtodName = "客户端新增用户")
    public ResultInfo<?> selfRegister(@Validated(value = {CreateGroup.class}) @RequestBody UserRequest request){
        TokenDTO token = userService.selfRegister(request);
        return ResultInfo.success(token);
    }

    @PostMapping(value = "register/cms")
    @LoginIntercept
    @AopLog(mehtodType = AopLog.MethodTypeEnum.ADD, mehtodName = "后台新增用户")
    public ResultInfo<?> cmsRegister(@Validated(value = {CreateGroup.class}) @RequestBody UserRequest request){
        UserInfo userInfo = ThreadLocalUser.getUserInfo();
        request.setUserInfo(userInfo);
        TokenDTO token = userService.cmsRegister(request);
        return ResultInfo.success(token);
    }

    @PostMapping(value = "login")
    public ResultInfo<?> login(@Validated(value = {RetrieveGroup.class})  @RequestBody UserLoginDTO request){
        TokenDTO token = userService.login(request);
        return ResultInfo.success(token);
    }

    @PostMapping(value = "account/check")
    public ResultInfo<?> checkAccount(@RequestBody UserLoginDTO request){
        UserInfoVo userInfoVo = userService.checkAccount(request);
        return ResultInfo.success(userInfoVo);
    }

}
