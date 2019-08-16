package com.face.permission.mapper.dao;

import com.face.permission.mapper.domain.PAccountDO;

public interface PAccountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PAccountDO record);

    int insertSelective(PAccountDO record);

    PAccountDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PAccountDO record);

    int updateByPrimaryKey(PAccountDO record);
}