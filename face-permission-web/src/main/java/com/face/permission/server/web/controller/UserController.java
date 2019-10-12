package com.face.permission.server.web.controller;

import com.face.permission.api.model.request.user.UserInfo;
import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.api.model.request.valids.groups.UpdateGroup;
import com.face.permission.common.responses.PaginatedResultData;
import com.face.permission.common.responses.ResultInfo;
import com.face.permission.mapper.query.user.UserQuery;
import com.face.permission.mapper.vo.user.UserInfoVo;
import com.face.permission.server.config.ThreadLocalUser;
import com.face.permission.server.config.annoations.AopLog;
import com.face.permission.server.config.annoations.LoginIntercept;
import com.face.permission.server.config.annoations.RepeatSubmitCheck;
import com.face.permission.service.interfaces.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-18 14:38
 */
@RestController
@RequestMapping(value = "user")
@Validated
public class UserController {

    @Autowired
    IUserService userService;

    @GetMapping(value = "check/{userId}")
    @RepeatSubmitCheck
    public ResultInfo<?> checkUser(@PathVariable(value = "userId") String userId) {
        UserInfo userInfo = ThreadLocalUser.getUserInfo();
        //加验签的方法
        UserInfoVo userInfoVo = userService.check(userId, userInfo);
        return ResultInfo.success(userInfoVo);
    }

    @PostMapping(value = "update")
    @LoginIntercept
    @RepeatSubmitCheck
    @AopLog(mehtodType = AopLog.MethodTypeEnum.UPDATE, mehtodName = "更新用户")
    public ResultInfo<?> updateUserInfo(@Validated(value = {UpdateGroup.class}) @RequestBody UserRequest request) {
        UserInfo userInfo = ThreadLocalUser.getUserInfo();
        request.setUserInfo(userInfo);
        Boolean result = userService.update(request);
        return ResultInfo.success(result);
    }

    @PostMapping(value = "query")
    public ResultInfo<PaginatedResultData<UserInfoVo>> queryUsers(@RequestBody UserQuery query) {

        Integer total = userService.getTotal(query);
        List<UserInfoVo> resultList = new ArrayList();
        if (total > 0) {
            resultList = userService.getList(query);
        }
        return ResultInfo.buildPaginatedResult(query, resultList, total);
    }

    @PostMapping(value = "delete/{userId}")
    @LoginIntercept
    @RepeatSubmitCheck
    @AopLog(mehtodType = AopLog.MethodTypeEnum.DELETE, mehtodName = "删除用户")
    public ResultInfo<?> delete(@PathVariable("userId") String userId) {
        return ResultInfo.success(userService.delete(ThreadLocalUser.getUserInfo(), userId));
    }
}
