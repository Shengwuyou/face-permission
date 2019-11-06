package com.face.permission.server.web.controller;

import com.face.permission.common.model.request.user.UserInfo;
import com.face.permission.common.model.request.user.UserRequest;
import com.face.permission.common.model.request.valids.groups.UpdateGroup;
import com.face.permission.common.annoations.AopLog;
import com.face.permission.common.annoations.LoginIntercept;
import com.face.permission.common.annoations.RepeatSubmitCheck;
import com.face.permission.common.responses.PaginatedResultData;
import com.face.permission.common.responses.ResultInfo;
import com.face.permission.mapper.query.user.UserQuery;
import com.face.permission.mapper.vo.user.UserInfoVo;
import com.face.permission.common.model.request.user.ThreadLocalUser;
import com.face.permission.service.interfaces.user.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

@Api(tags = "用户管理模块")
@RestController
@RequestMapping(value = "user")
@Validated
public class UserController {

    @Autowired
    IUserService userService;

    @ApiOperation(value = "检查用户信息", notes = "根据用户id查询用户信息", httpMethod = "GET")
    @GetMapping(value = "check/{userId}")
    @LoginIntercept
    @RepeatSubmitCheck
    public ResultInfo<?> checkUser(@PathVariable(value = "userId") String userId) {
        UserInfo userInfo = ThreadLocalUser.getUserInfo();
        //加验签的方法
        UserInfoVo userInfoVo = userService.check(userId, userInfo);
        return ResultInfo.success(userInfoVo);
    }

//    @ApiOperation(value = "检查用户自己信息", notes = "根据用户id查询用户信息", httpMethod = "GET")
//    @GetMapping(value = "info")
//    @LoginIntercept
//    @RepeatSubmitCheck
//    public ResultInfo<?> userInfo() {
//        UserInfo userInfo = ThreadLocalUser.getUserInfo();
//        //加验签的方法
//        UserInfoVo userInfoVo = userService.check(null, userInfo);
//        return ResultInfo.success(userInfoVo);
//    }

    @ApiOperation(value = "更新用户信息", notes = "更新用户信息", httpMethod = "POST")
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


    @ApiOperation(value = "用户推荐好友列表刷新")
    @PostMapping(value = "recommend/friend")
    @LoginIntercept
    @RepeatSubmitCheck
    public ResultInfo<?> queryFriends() {
        return ResultInfo.success("OK");
    }
}
