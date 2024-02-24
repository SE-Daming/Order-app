package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Employee;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;
    /**
     * 新增菜品、菜品口味
     */
    @Override
    public void save(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.save(dish);

        Long dishId=dishMapper.getIdByName(dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.stream().forEach(flavor->{
            flavor.setDishId(dishId);
        });
    }

    /**
     * 菜品分页查询
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        Page<DishVO> page=dishMapper.pageQuery(dishPageQueryDTO);

        long total=page.getTotal();

        List<DishVO>records=page.getResult();

        return new PageResult(total,records);
    }

    /**
     * 按id查询菜品
     */
    @Override
    public DishVO getById(Long id) {
        return dishMapper.getById(id);
    }

    /**
     * 按id删除菜品
     * ids形式为ids=1,2,3
     */
    @Override
    public void deleteById(String ids) {
        String[] id = ids.split(",");
        for(String i:id){
            dishMapper.deleteById(i);
        }
    }

    /**
     * 修改菜品、涉及到菜品和菜品口味两张表
     */
    @Override
    public void modify(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.stream().forEach(flavor->{
            flavor.setDishId(dishDTO.getId());
        });
    }

    /**
     * 修改菜品状态
     * 和modify共享一个mapper方法
     */
    @Override
    public void changeStatus(int status,Long id) {
        Dish dish=Dish
                .builder()
                .status(status)
                .id(id).build();
        dishMapper.update(dish);
    }
}
