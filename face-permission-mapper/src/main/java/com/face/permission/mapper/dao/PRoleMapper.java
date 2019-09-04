package com.face.permission.mapper.dao;

import com.face.permission.mapper.domain.PRoleDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PRoleDo record);

    int insertSelective(PRoleDo record);

    PRoleDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PRoleDo record);

    int updateByPrimaryKey(PRoleDo record);

    /**
     *     查询当前系统中type为指定的权限集合
     */
    List<PRoleDo> selectByType(@Param("type") Integer type);
    /**
     *     查询当前系统中类型不为type的权限集合
     */
    List<PRoleDo> selectNotType(@Param("type") Integer type);

    /**
     *     查询当前系统中存在
     */
    List<PRoleDo> selectAll();
}