package com.face.permission.mapper.dao;

import com.face.permission.mapper.domain.PAccount;

public interface PAccountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PAccount record);

    int insertSelective(PAccount record);

    PAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PAccount record);

    int updateByPrimaryKey(PAccount record);
}