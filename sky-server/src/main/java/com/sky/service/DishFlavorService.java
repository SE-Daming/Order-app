package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishFlavorService {
    void save(DishDTO dishDTO);

    void modify(DishDTO dishDTO);
}
