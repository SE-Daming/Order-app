package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    /**
     * 按套餐id查询菜品id
     */
    @Select("select dish_id from setmeal_dish where setmeal_id=#{setmealId}")
    List<Long>getDishIdBySetmealId(Long setmealId);

}
