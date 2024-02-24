package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealDishMapper {
    @Insert("insert into setmeal_dish (setmeal_id,dish_id,name,price,copies) values (#{setmealId},#{dishId}," +
            "#{name},#{price},#{copies})")
    void save(SetmealDish setmealDish);

    /**
     * 修改套餐时根据菜品id删除该套餐原有的菜品
     * @param id
     */
    @Delete("delete  from setmeal_dish where setmeal_id=#{id}")
    void deleteBySetmealId(Long id);
}
