package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 菜品新增时的口味保存
     * @param flavor
     */
    @Insert("INSERT INTO dish_flavor (dish_id, name, value)\n" +
            "    VALUES (#{dishId}, #{name}, #{value})")
    void save(DishFlavor flavor);


    /**
     * 修改dish的flavor
     * @param dishFlavor
     */
    @Update("update dish_flavor set dish_id=#{dishId},name=#{name},value=#{value}" +
            "where dish_id=#{dishId}")
    void update(DishFlavor dishFlavor);

    /**
     * 按dishId查询口味
     * @param dishId
     * @return
     */
    @Select("select value from dish_flavor where dish_id in (#{dishId})")
    String getByDishIds(List<Long> dishId);
}
