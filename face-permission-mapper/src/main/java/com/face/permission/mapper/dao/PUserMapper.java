package com.face.permission.mapper.dao;

import com.face.permission.common.responses.PageQuery;
import com.face.permission.mapper.domain.PUserDO;
import com.face.permission.mapper.dto.request.UserLoginDTO;
import com.face.permission.mapper.query.user.UserQuery;
import com.face.permission.mapper.vo.user.UserInfoVo;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Repository
public interface PUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(PUserDO record);

    int insertSelective(PUserDO record);

    PUserDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUserDO record);

    /**
     * 更新用户状态，以此来注销，逻辑删除用户
     * @param uId
     * @param status
     * @return
     */
    int updateStatus(@Param("uId") String uId, @Param("status") Integer status);

    int updateByPrimaryKey(PUserDO record);

    /**
     * 根据type查询登陆信息是否存在
     * @param userLogin
     * @return
     */
    PUserDO selectByLoginType(UserLoginDTO userLogin);

    /**
     *
     * @param uId
     * @return
     */
    PUserDO selectByUID(String uId);

    Integer selectUserTotal(UserQuery query);

    List<UserInfoVo> selectUsers(UserQuery query);

    List<PUserDO> selectRegisterUsers(@Param(value = "mobile") String mobile, @Param(value = "email") String email);
}