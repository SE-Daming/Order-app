package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishFlavorServiceImpl implements DishFlavorService {
    @Autowired
    DishFlavorMapper dishFlavorMapper;

    /**
     * 用于dish新增时flavor的新增
     * @param dishDTO
     */
    @Override
    public void save(DishDTO dishDTO) {
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.stream().forEach(flavor->{
            dishFlavorMapper.save(flavor);}
        );
    }

    /**
     * 修改dish的flavor
     */
    @Override
    public void modify(DishDTO dishDTO) {
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.stream().forEach(flavor->{
            dishFlavorMapper.update(flavor);
        });
    }
}
