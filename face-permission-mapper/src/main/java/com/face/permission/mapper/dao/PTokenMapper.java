package com.face.permission.mapper.dao;

import com.face.permission.mapper.domain.PToken;

public interface PTokenMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PToken record);

    int insertSelective(PToken record);

    PToken selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PToken record);

    int updateByPrimaryKey(PToken record);
}