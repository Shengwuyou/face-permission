package com.face.permission.mapper.dao;

import com.face.permission.mapper.domain.PUserDO;
import com.face.permission.mapper.dto.request.UserLoginDTO;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;

@Repository
public interface PUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(PUserDO record);

    int insertSelective(PUserDO record);

    PUserDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUserDO record);

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
}