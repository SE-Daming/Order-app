package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Api(tags = "用户端菜品管理")
public class DishController {

    @Autowired
    DishService dishService;
    @GetMapping("list")
    @ApiOperation(value = "根据categoryId展示菜品")
    public Result<List<DishVO>>listDish(Long categoryId){
            List<DishVO>list=dishService.getByCategoryId(categoryId);
            return Result.success(list);
    }
}
