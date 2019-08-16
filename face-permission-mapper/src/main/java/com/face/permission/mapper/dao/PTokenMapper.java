package com.face.permission.mapper.dao;

import com.face.permission.mapper.domain.PTokenDO;

public interface PTokenMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PTokenDO record);

    int insertSelective(PTokenDO record);

    PTokenDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PTokenDO record);

    int updateByPrimaryKey(PTokenDO record);
}