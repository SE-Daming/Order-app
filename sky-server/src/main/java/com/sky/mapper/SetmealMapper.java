package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {
    /**
     * 新增套餐
     * @param setmeal
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into setmeal (category_id,name,price,status,description,image, create_time, update_time, create_user, update_user)" +
            "values (#{categoryId},#{name},#{price},#{status}," +
            "#{description},#{image}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void save(Setmeal setmeal);

    /**
     * 按套餐名称查询套餐id
     * @param name
     * @return
     */
    @Select("select id from setmeal where name=#{name}")
    Long getIdByName(String name);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO>pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Select("select * from setmeal where id=#{id}")
    SetmealVO getById(Long id);

    /**
     * 根据id删除套餐
     * @param i
     */
    @Delete("delete from setmeal where id=#{i}")
    void deleteById(String i);

    /**
     * 修改套餐、套餐状态
     * 两个功能公用接口
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);
}
