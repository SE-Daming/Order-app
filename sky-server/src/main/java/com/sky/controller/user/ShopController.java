package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺管理")
public class ShopController {
    @GetMapping("status")
    @ApiOperation(value = "获取店铺状态")
    public Result<Integer>getStatus(){
        return Result.success(1);
    }
}
