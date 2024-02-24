package com.sky.service;

import com.sky.dto.SetmealDTO;
import org.springframework.stereotype.Service;

public interface SetmealDishService {
    void save(SetmealDTO setmealDTO);

    void deleteById(String ids);
}
