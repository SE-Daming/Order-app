package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺管理")
public class ShopController {
    public static final String key="SHOP_STATUS";

    @Autowired
    RedisTemplate redisTemplate;


    @GetMapping("status")
    @ApiOperation(value = "获取店铺状态")
    public Result<Integer>getStatus(){
        Object status = redisTemplate.opsForValue().get(key);
        return Result.success((Integer) status);

    }
}
