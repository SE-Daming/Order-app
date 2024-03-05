package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user/user/")
@Slf4j
@Api(tags = "用户端相关接口")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtProperties jwtProperties;

    @PostMapping("login")
    public Result<UserLoginVO>login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("userLoginDTO:{}",userLoginDTO);

        //调用微信接口服务
        User user=userService.wxLogin(userLoginDTO);

        //生成token
        String userSecretKey = jwtProperties.getUserSecretKey();

        long userTtl = jwtProperties.getUserTtl();

        Map<String,Object>claims=new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());

        String token= JwtUtil.createJWT(userSecretKey,userTtl,claims);

        //创建返回的对象
        UserLoginVO userLoginVO=UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }
}