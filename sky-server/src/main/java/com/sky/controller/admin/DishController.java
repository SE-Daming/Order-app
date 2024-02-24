package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishFlavorService;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理")
public class DishController {
    @Autowired
    DishService dishService;
    @Autowired
    DishFlavorService dishFlavorService;

    /**
     * 新增菜品、涉及到dish和dish_flavor两张表
     *
     * Q:dish的主键id是数据库自动生成的、而flavor的dishId不能为null、如何再保存flavor前获取到该dish的id？
     * A:根据dish的name查询到id然后基于stream给每一个flavor的dishId赋值
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO){
        dishService.save(dishDTO);
        dishFlavorService.save(dishDTO);
        return Result.success();
    }

    /**
     * 分页查询、根据分类id查询商品
     * @param dishPageQueryDTO
     * @return
     */

    @GetMapping("page")
    @ApiOperation(value = "菜品分页查询")
    public Result<PageResult>pageQuery(DishPageQueryDTO dishPageQueryDTO){
        PageResult pageResult=dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 按id查询dish
     * 用于修改菜品时回显菜品
     */
    @GetMapping("{id}")
    @ApiOperation(value = "按id查询菜品")
    public Result<DishVO>echo(@PathVariable Long id){
        DishVO dishVO=dishService.getById(id);
        return Result.success(dishVO);
    }

    /**
     * 删除菜品
     */
    @DeleteMapping
    @ApiOperation(value = "删除菜品")
    public Result deleteById(String ids){
        dishService.deleteById(ids);
        return  Result.success();
    }

    /**
     *修改菜品
     */
    @PutMapping
    @ApiOperation(value = "修改菜品")
    public Result modifyDish(@RequestBody DishDTO dishDTO){
        dishService.modify(dishDTO);
        dishFlavorService.modify(dishDTO);
        return Result.success();
    }

    /**
     * 菜品停售起售
     */
    @PostMapping("status/{status}")
    @ApiOperation(value = "修改菜品状态")
    public Result changeStatus(@PathVariable int status,Long id){
        dishService.changeStatus(status,id);
        return Result.success();
    }

    @GetMapping("list")
    @ApiOperation(value = "根据分类id查询菜品")
    public Result<List<DishVO>>selectByCategoryId(Long categoryId){
        List<DishVO> dishVO=dishService.getByCategoryId(categoryId);
        return Result.success(dishVO);
    }
}
