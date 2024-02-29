package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    SetmealMapper setmealMapper;

    @Autowired
    SetmealDishMapper setmealDishMapper;

    @Autowired
    DishMapper dishMapper;
    /**
     * 新增套餐、涉及到套餐和套餐食品两张表
     */
    @Override
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.save(setmeal);

        Long setmealId=setmealMapper.getIdByName(setmeal.getName());
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.stream().forEach(setmealDish->{
            setmealDish.setSetmealId(setmealId);
        });
    }

    /**
     * 套餐分页查询
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page=setmealMapper.pageQuery(setmealPageQueryDTO);
        long total=page.getTotal();
        List<SetmealVO>records=page.getResult();
        return new PageResult(total,records);
    }

    /**
     * 根据id查询套餐
     */
    @Override
    public SetmealVO getById(Long id) {
        return setmealMapper.getById(id);
    }

    /**
     * 根据id删除套餐
     */
    @Override
    public void deleteById(String ids) {
        String[]id=ids.split(",");
        Arrays.asList(id).stream().forEach(i->{
            setmealMapper.deleteById(i);
        });
    }

    /**
     * 修改套餐
     */
    @Override
    public void modify(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
    }

    /**
     * 起售、停售
     */
    @Override
    public void modifyStatus(int status, Long id) {
        Setmeal setmeal=Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 根据分类id查询套餐
     */
    @Override
    public List<SetmealVO> getByCategoryId(Long categoryId) {
        return setmealMapper.getByCategoryId(categoryId);
    }

    /**
     * 根据套餐id查询菜品
     */
    @Override
    public List<DishVO> getDishById(Long id) {
        List<DishVO>list=new ArrayList<>();
        List<Long> dishIds = setmealDishMapper.getDishIdBySetmealId(id);
        dishIds.stream().forEach(dishId->{
            DishVO dishVO = dishMapper.getById(dishId);
            list.add(dishVO);
        });
        return list;
    }
}
