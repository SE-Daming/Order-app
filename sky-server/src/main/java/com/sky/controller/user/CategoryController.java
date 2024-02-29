package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "用户端分类管理")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @ApiOperation(value = "用户端展示分类管理")
    @GetMapping("list")
    public Result<List<Category>>listCategory(){
       List<Category>list= categoryService.selectAll();
       return Result.success(list);
    }
}
