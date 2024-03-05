package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/shop")
@Api(tags = "管理端店铺管理")
public class ShopController {
    @GetMapping("status")
    @ApiOperation(value = "获取营业状态")
    public Result<Integer> getStatus(){
        return null;//TODO 用redis查询和更改
    }
}
