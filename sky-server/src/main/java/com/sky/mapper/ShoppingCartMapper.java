package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 返回购物车的所有商品
     * @return
     */
    @Select("select * from shopping_cart")
    List<ShoppingCart> selectAll();

    /**
     * 新插入购物车
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart (name,image,user_id,dish_id,setmeal_id,dish_flavor,number,amount,create_time)" +
            "values (#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")

    void save(ShoppingCart shoppingCart);

    /**
     * 购物车内商品数量加一
     */
    void add(ShoppingCart shoppingCart);
    /**
     * 购物车内商品减一
     */
    void subOne(ShoppingCartDTO shoppingCartDTO);

    /**
     * 删除购物车内商品
     * @param shoppingCartDTO
     */
    void delete(ShoppingCartDTO shoppingCartDTO);

    /**
     * 按name查询购物车
     * @param name
     * @return
     */
    @Select("select * from shopping_cart where name=#{name}")
    ShoppingCart getByName(String name);

    /**
     * 按套餐id查询数量
     * @param setmealId
     * @return
     */
    @Select("select number from shopping_cart where setmeal_id=#{setmealId}")
    Integer getNumberBySetmealId(Long setmealId);//TODO:查不到返回什么？一开始用int作为返回值，查不到会异常

    /**
     * 按菜品id查询数量
     * @param dishId
     * @return
     */
    @Select("select number from shopping_cart where dish_id=#{dishId}")

    Integer getNumberByDishId(Long dishId);

    /**
     * 删除所有商品
     */
    @Delete("delete * from shoppig_cart")
    void clean();
}
