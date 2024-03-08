package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 菜品新增
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    @Insert("INSERT INTO dish (name, category_id, price, image, description, status, create_time, update_time, create_user, update_user)\n" +
            "    VALUES (#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void save(Dish dish);

    /**
     * 按dish的name查询id
     * @param dish
     * @return
     */
    @Select("select id from dish where name=#{name}")
    Long getIdByName(Dish dish);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 按id查询菜品，用于修改菜品时回显数据
     * @param id
     * @return
     */
    DishVO getById(Long id);

    void deleteById(String i);

    /**
     * 用于修改菜品状态、修改菜品、共用一个接口
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    @Select("select * from dish where category_id=#{categoryId}")
    List<DishVO> getByCategoryId(Long categoryId);

    /**
     * 根据id查询价格
     * @param dishId
     * @return
     */
    @Select("select price from dish where id=#{dishId}")
    BigDecimal getPriceById(Long dishId);

    /**
     * 计算给定状态的数量
     * @return
     */
    @Select("select count(*) from dish where status=#{status}")
    Integer getNumOfStatus(Integer status);
}
