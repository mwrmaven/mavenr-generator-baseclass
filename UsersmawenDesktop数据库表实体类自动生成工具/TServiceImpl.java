package com.maven.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.maven.entity.T;
import com.maven.mapper.TMapper;
import com.maven.vo.TVO;
import com.maven.service.TService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TServiceImpl implements TService {

    @Autowired
    private TMapper tMapper;

    /**
     * 动态分页查询数据集
     * @param t
     * @return
     */
    @Override
    public PageInfo<T> selectByModel(TVO tVO) {
        T t = T.builder()
                .id(tVO.getId())
                .name(tVO.getName())
                .age(tVO.getAge())
                .build();
        Page<Object> objects = PageHelper.startPage(tVO.getPageNum(), tVO.getPageSize());
        List<T> all = tMapper.selectByModel(t);
        PageInfo<T> result = PageInfo.of(all);
        result.setTotal(objects.getTotal());
        result.setPages(objects.getPages());
        return result;
    }

    /**
     * 动态查询数据集
     * @param t
     * @return
     */
    @Override
    public List<T> selectList(T t) {
        List<T> all = tMapper.selectByModel(t);
        return all;
    }

    /**
     * 动态插入数据
     * @param t
     * @return
     */
    @Override
    public int insertSelective(T t) {
        int result = tMapper.insertSelective(t);
        return result;
    }

    /**
     * 批量插入数据
     * @param insertList
     * @return
     */
    @Override
    public int insertList(List<T> insertList) {
        int result = tMapper.insertList(insertList);
        return result;
    }

    /**
     * 动态更新数据
     * @param t
     * @return
     */
    @Override
    public int updateByPrimaryKeySelective(T t) {
        int result = tMapper.updateByPrimaryKeySelective(t);
        return result;
    }

}
