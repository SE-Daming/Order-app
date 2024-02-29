package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setStatus(1);
        categoryMapper.save(category);
    }

    /**
     * 分类分页查询
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category>page=categoryMapper.pageQuery(categoryPageQueryDTO);
        long total = page.getTotal();
        List<Category>records=page.getResult();
        return new PageResult(total,records);
    }

    /**
     * 修改分类
     */
    @Override
    public void modifyCategory(CategoryDTO categoryDTO) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        categoryMapper.modifyCategory(category);
    }

    /**
     * 启用、禁用分类
     */
    @Override
    public void changeStatus(int status, Long id) {
        Category category=Category.builder()
                .status(status)
                .id(id)
                .build();
        categoryMapper.modifyCategory(category);
    }

    /**
     * 根据id删除分类
     */
    @Override
    public void deleteById(Long id) {
        categoryMapper.deleteById(id);
    }

    /**
     * 根据类型查询分类
     * TODO：前端界面没发现这个功能，是分页查询里的吗？
     */
    @Override
    public List<Category> findByType(int type) {
        return categoryMapper.findByType(type);
    }

    /**
     * 小程序端查询分类信息
     */
    @Override
    public List<Category> selectAll() {
        return categoryMapper.selectAll();
    }
}
