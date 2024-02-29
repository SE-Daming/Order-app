package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.*;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    ShoppingCartMapper shoppingCartMapper;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;
    /**
     * 查看购物车
     */
    @Override
    public List<ShoppingCart> selectAll() {
        return shoppingCartMapper.selectAll();
    }

    /**
     * 向购物车增加商品
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        shoppingCart.setCreateTime(LocalDateTime.now());

        List<Long>dishId=new ArrayList<>();
        //如果是套餐
        if(shoppingCartDTO.getDishId()==null){
            BigDecimal amount=setmealMapper.getPriceById(shoppingCartDTO.getSetmealId());
            shoppingCart.setAmount(amount);
            shoppingCart.setName(setmealMapper.getById(shoppingCartDTO.getSetmealId()).getName());

            //查询套餐内菜品以便确定口味
            dishId=setmealDishMapper.getDishIdBySetmealId(shoppingCartDTO.getSetmealId());
        }

        //如果是菜品
        else {
            BigDecimal amount=dishMapper.getPriceById(shoppingCartDTO.getDishId());
            shoppingCart.setAmount(amount);
            shoppingCart.setName(dishMapper.getById(shoppingCartDTO.getDishId()).getName());
            dishId.add(shoppingCartDTO.getDishId());
        }

        //设置购物车内商品的口味
        String flavor=dishFlavorMapper.getByDishIds(dishId);
        shoppingCart.setDishFlavor(flavor);

        ShoppingCart shoppingCart1=shoppingCartMapper.getByName(shoppingCart.getName());
        //如果购物车已经存在
        if(shoppingCart1!=null){
            shoppingCartMapper.add(shoppingCart);
            return;
        }
        //不存在则把数量设为1 并插入
        shoppingCart.setNumber(1); //TODO:为什么这里的参数会影响用户端？把它设为10 用户加入购物车时数量是10
        shoppingCartMapper.save(shoppingCart);
    }

    /**
     * 删除购物车商品
     * 即使数量减去1
     */
    @Override
    public void delete(ShoppingCartDTO shoppingCartDTO) {
        Integer number=0;

        if(shoppingCartDTO.getSetmealId()!=null){
            number=shoppingCartMapper.getNumberBySetmealId(shoppingCartDTO.getSetmealId());
        }
        else if(shoppingCartDTO.getDishId()!=null){
            number=shoppingCartMapper.getNumberByDishId(shoppingCartDTO.getDishId());
        }
        //当购物车展示与数据库不一致时,购物车存在、数据库没有
        if(number==null) {
            return;
        }
        if(number==1){
            shoppingCartMapper.delete(shoppingCartDTO);
            return;
        }
        shoppingCartMapper.subOne(shoppingCartDTO);
    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        shoppingCartMapper.clean();
    }
}
