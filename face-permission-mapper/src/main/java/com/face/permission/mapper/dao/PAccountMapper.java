package com.face.permission.mapper.dao;

import com.face.permission.mapper.domain.PAccountDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PAccountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PAccountDO record);

    int insertSelective(PAccountDO record);

    PAccountDO selectByPrimaryKey(Long id);

    /**
     * 根据用户id获取账号信息
     * @param uId
     * @return
     */
    PAccountDO selectByUserId(@Param("uId") String uId,
                              @Param("loginName") String loginName,
                              @Param("password") String password);

    int updateByPrimaryKeySelective(PAccountDO record);

    int updateByPrimaryKey(PAccountDO record);
}