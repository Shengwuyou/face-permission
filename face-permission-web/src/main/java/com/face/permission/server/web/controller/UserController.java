package com.face.permission.server.web.controller;

import com.face.permission.api.model.request.user.UserInfo;
import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.api.model.request.valids.groups.CreateGroup;
import com.face.permission.api.model.request.valids.groups.RetrieveGroup;
import com.face.permission.api.model.response.TokenDTO;
import com.face.permission.common.responses.ResultInfo;
import com.face.permission.mapper.dto.request.UserLoginDTO;
import com.face.permission.server.config.ThreadLocalUser;
import com.face.permission.server.config.annoations.LoginIntercept;
import com.face.permission.service.interfaces.user.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import javax.websocket.server.PathParam;

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
        TokenDTO token = userService.cmsRegister(request);
        return ResultInfo.success(token);
    }


    @RequestMapping(value = "login" ,method = RequestMethod.POST)
    @ResponseBody
    @LoginIntercept(require = false)
    //TODO AOP加操作日志
    public ResultInfo<?> login(@Validated(value = {RetrieveGroup.class})  @RequestBody UserRequest request){
        UserLoginDTO loginDTO = new UserLoginDTO();
        BeanUtils.copyProperties(request, loginDTO);
        TokenDTO token = userService.login(loginDTO);
        return ResultInfo.success(token);
    }

    @RequestMapping(value = "update" ,method = RequestMethod.GET)
    @ResponseBody
    //TODO AOP加操作日志
    public ResultInfo<?> updateUserInfo(@NotBlank(message = "姓名不能为空")
                                        @Pattern(regexp = "^.{2,12}", message = "姓名格式不规范")
                                        @PathParam("name") String name,
                                        @Min(value = 14, message = "年龄须大于14")
                                        @Max(value = 99 , message = "年龄须小于99")
                                        @PathParam("age") Integer age){
        Assert.isTrue(true, "我感觉你是错的，哈哈哈"+ name + age);
        String token = "ok";
        return ResultInfo.success(token);
    }
}
