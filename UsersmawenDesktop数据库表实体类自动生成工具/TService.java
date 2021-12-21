package com.maven.service;

import com.github.pagehelper.PageInfo;
import com.maven.entity.T;
import com.maven.vo.TVO;

import java.util.List;

public interface TService {

    /**
     * 动态分页查询数据集
     * @param t
     * @return
     */
    PageInfo<T> selectByModel(TVO tVO);

    /**
     * 动态查询数据集
     * @param t
     * @return
     */
    List<T> selectList(T t);

    /**
     * 动态插入数据
     * @param t
     * @return
     */
    int insertSelective(T t);

    /**
     * 批量插入数据
     * @param insertList
     * @return
     */
    int insertList(List<T> insertList);

    /**
     * 动态更新数据
     * @param t
     * @return
     */
    int updateByPrimaryKeySelective(T t);

}