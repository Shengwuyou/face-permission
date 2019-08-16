package com.face.permission.mapper.dao;

import com.face.permission.mapper.domain.PUserDO;
import org.springframework.stereotype.Repository;

@Repository
public interface PUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUserDO record);

    int insertSelective(PUserDO record);

    PUserDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUserDO record);

    int updateByPrimaryKey(PUserDO record);
}