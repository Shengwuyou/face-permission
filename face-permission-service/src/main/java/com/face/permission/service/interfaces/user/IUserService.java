package com.face.permission.service.interfaces.user;

import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.api.model.response.TokenDTO;
import com.face.permission.mapper.domain.PUserDO;
import com.face.permission.mapper.dto.request.UserLoginDTO;
import com.face.permission.mapper.query.user.UserQuery;
import com.face.permission.mapper.vo.user.UserInfoVo;

import java.util.List;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-18 15:21
 */
public interface IUserService {

    /**
     * face 用户自定义注册
     * @param request
     * @return
     */
    TokenDTO selfRegister(UserRequest request);

    /**
     * 后台用户导入注册
     * @param request
     * @return
     */
    TokenDTO cmsRegister(UserRequest request);

    /**
     * 登陆
     * @param request
     * @return
     */
    TokenDTO login(UserLoginDTO request);

    /**
     * 更新用户信息
     * @param request
     * @return
     */
    boolean update(UserRequest request);


    Integer getTotal(UserQuery query);

    List<UserInfoVo> getList(UserQuery query);
}
