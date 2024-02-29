package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "用户端套餐管理")
public class SetmealController {
    @Autowired
    SetmealService setmealService;

    @GetMapping("list")
    @ApiOperation(value = "套餐展示")
    public Result<List<SetmealVO>>list(Long categoryId){
        List<SetmealVO> list=setmealService.getByCategoryId(categoryId);
        return Result.success(list);
    }

    @GetMapping("dish/{id}")
    @ApiOperation("根据套餐id查询菜品")
    public Result<List<DishVO>>getDishById(@PathVariable Long id){
        List<DishVO>list=setmealService.getDishById(id);
        return Result.success(list);
    }
}
