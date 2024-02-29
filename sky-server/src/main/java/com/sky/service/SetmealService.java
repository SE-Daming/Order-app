package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    void save(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    SetmealVO getById(Long id);

    void deleteById(String ids);

    void modify(SetmealDTO setmealDTO);

    void modifyStatus(int status, Long id);

    List<SetmealVO> getByCategoryId(Long categoryId);

    List<DishVO> getDishById(Long id);
}
