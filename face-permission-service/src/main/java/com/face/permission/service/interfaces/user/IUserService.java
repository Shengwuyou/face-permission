package com.face.permission.service.interfaces.user;

import com.face.permission.api.model.request.user.UserInfo;
import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.api.model.response.TokenDTO;
import com.face.permission.mapper.domain.PAccountDO;
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
     * 查询用户详细信息
     * @param userId
     * @param userInfo
     * @return
     */
    UserInfoVo check(String userId, UserInfo userInfo);

    /**
     * 获取用户基本信息
     * @param userId
     * @return
     */
    PUserDO getUser(String userId);

    /**
     * 保存用户信息
     * @param userDO
     */
    void setUser(PUserDO userDO);
    /**
     * 获取用户账号信息
     * @param userId
     * @return
     */
    PAccountDO getAccount(String userId);

    /**
     * 更新用户信息
     * @param request
     * @return
     */
    boolean update(UserRequest request);


    Integer getTotal(UserQuery query);

    List<UserInfoVo> getList(UserQuery query);

    /**
     * 销户
     * @param userId
     * @return
     */
    boolean delete(UserInfo userInfo, String userId);

    void test();
}
