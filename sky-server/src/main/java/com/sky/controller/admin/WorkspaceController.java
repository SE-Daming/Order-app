package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("admin/workspace")
@Api(tags = "工作台管理")
public class WorkspaceController {
    @Autowired
    WorkspaceService workspaceService;
    @GetMapping("businessData")
    @ApiOperation(value = "查询今日运营数据")
    public Result<BusinessDataVO>todayData(){
        LocalDate localDate=LocalDate.now();
        BusinessDataVO businessDataVO=workspaceService.todayData(localDate);
        return Result.success(businessDataVO);
    }

    @GetMapping("overviewSetmeals")
    @ApiOperation(value = "套餐总览")
    public Result<SetmealOverViewVO>setmealOverView(){
        SetmealOverViewVO setmealOverViewVO=workspaceService.setmealOverview();
        return Result.success(setmealOverViewVO);
    }

    @GetMapping("overviewDishes")
    @ApiOperation(value = "菜品总览")
    public Result<DishOverViewVO>dishOverView(){
        DishOverViewVO dishOverViewVO=workspaceService.dishOverview();
        return Result.success(dishOverViewVO);
    }

    @GetMapping("overviewOrders")
    @ApiOperation(value = "订单数据管理")
    public Result<OrderOverViewVO>orderOverview(){
        OrderOverViewVO orderOverViewVO=workspaceService.orderOverview();
        return Result.success(orderOverViewVO);
    }
}
