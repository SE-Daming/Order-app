package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop")
@Api(tags = "管理端店铺管理")
@Slf4j
public class ShopController {
    public static final String key="SHOP_STATUS";

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("status")
    @ApiOperation(value = "获取营业状态")
    public Result<Integer> getStatus(){
        Object status = redisTemplate.opsForValue().get(key);
        if(status==null){
            redisTemplate.opsForValue().set(key,1);//若未设置状态默认为1
        }
        return Result.success((Integer) status);
    }

    @PutMapping("{status}")
    @ApiOperation(value = "设置店铺状态")
    public Result setStatus(@PathVariable int status){
        Long id = BaseContext.getCurrentId();
        redisTemplate.opsForValue().set(key,status);
        return Result.success();
    }
}
