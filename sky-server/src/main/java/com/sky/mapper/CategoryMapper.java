package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 新增分类
     * @param category
     */
    @Insert("insert into category (id,type,name,sort,status, create_time, update_time, create_user, update_user)values (#{id},#{type},#{name},#{sort},#{status}" +
            ", #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void save(Category category);

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 修改分类
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void modifyCategory(Category category);

    /**
     * 根据id删除分类
     * @param id
     */
    @Delete("delete from category where id =#{id}")
    void deleteById(Long id);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @Select("select * from category where type=#{type}")
    List<Category> findByType(int type);

    /**
     * 小程序端查询分类信息
     */
    @Select("select * from category")
    List<Category> selectAll();
}
