package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类管理")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /**
     * 新增分类
     */
    @PostMapping
    @ApiOperation(value = "新增分类")
    public Result addCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 分页查询
     */
    @GetMapping("page")
    @ApiOperation(value = "分类分页查询")
    public Result<PageResult>pageQuery(CategoryPageQueryDTO categoryPageQueryDTO){
        PageResult pageResult=categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 修改分类
     */
    @PutMapping
    @ApiOperation(value = "修改分类")
    public Result modifyCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.modifyCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 启用、禁用分类
     */
    @PostMapping("status/{status}")
    @ApiOperation(value = "启用、禁用分类")
    public Result changeStatus(@PathVariable int status,Long id){
        categoryService.changeStatus(status,id);
        return Result.success();
    }

    /**
     * 根据id删除分类
     */
    @DeleteMapping
    @ApiOperation(value = "根据id删除分类")
    public Result deleteById(Long id){
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 根据类型查询分类
     */
    @GetMapping("list")
    @ApiOperation(value = "根据类型查询分类")
    public Result<List<Category>>findByType(int type){
        List<Category>list=categoryService.findByType(type);
        return Result.success(list);
    }
}
