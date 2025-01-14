package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    List<ShoppingCart> selectAll();

    void add(ShoppingCartDTO shoppingCartDTO);

    void delete(ShoppingCartDTO shoppingCartDTO);

    void clean();
}
