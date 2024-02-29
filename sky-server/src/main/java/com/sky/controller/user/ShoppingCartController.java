package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Api(tags = "购物车管理")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;
    @GetMapping("list")
    @ApiOperation(value = "查看购物车")
    public Result<List<ShoppingCart>>list(){
        List<ShoppingCart>list=shoppingCartService.selectAll();
        return Result.success(list);
    }

    @PostMapping("add")
    @ApiOperation(value = "向购物车增加商品")
    public Result addGoods(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    @PostMapping("sub")
    @ApiOperation(value = "删除购物车商品")
    public Result deleteGoods(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.delete(shoppingCartDTO);
        return Result.success();
    }

    @DeleteMapping("clean")
    @ApiOperation(value = "清空购物车")
    public Result cleanGoods(){
        shoppingCartService.clean();
        return Result.success();
    }
}
