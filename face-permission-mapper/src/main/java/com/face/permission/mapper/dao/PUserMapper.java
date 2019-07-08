package com.face.permission.mapper.dao;

import com.face.permission.mapper.domain.PUser;
import org.springframework.stereotype.Repository;

@Repository
public interface PUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUser record);

    int insertSelective(PUser record);

    PUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUser record);

    int updateByPrimaryKey(PUser record);
}