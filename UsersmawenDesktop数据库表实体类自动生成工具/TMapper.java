package com.maven.mapper;

import com.maven.entity.T;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TMapper {

    /**
     * 通用——动态查询数据
     * @param t
     * @return
     */
    List<T> selectByModel(T t);

    /**
     * 通用——动态插入数据
     * @param t
     * @return
     */
    int insertSelective(T t);

    /**
     * 通用——批量插入数据
     * @param insertList
     * @return
     */
    int insertList(List<T> insertList);

    /**
     * 动态更新——根据主键更新
     * @param t
     * @return
     */
    int updateByPrimaryKeySelective(T t);

}