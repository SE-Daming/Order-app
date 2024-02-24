package com.sky.service.impl;

import com.sky.dto.SetmealDTO;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.service.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
public class SetmealDishServiceImpl implements SetmealDishService{
    @Autowired
    SetmealDishMapper setmealDishMapper;
    /**
     * 新增套餐的食品
     */
    @Override
    public void save(SetmealDTO setmealDTO) {
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());
        setmealDishes.stream().forEach(setmealDish->{
            setmealDish.setSetmealId(setmealDTO.getId());
            setmealDishMapper.save(setmealDish);
        });
    }

    /**
     * 删除套餐的菜品
     */
    @Override
    public void deleteById(String ids) {
        String[]id=ids.split(",");
        Arrays.asList(id).stream().forEach(i->{
            setmealDishMapper.deleteBySetmealId(Long.valueOf(i));
        });
    }
}
