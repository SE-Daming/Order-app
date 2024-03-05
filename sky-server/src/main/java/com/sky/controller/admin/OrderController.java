package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
@Api(tags = "管理端订单管理")
@Slf4j
public class OrderController {
    @Autowired
    OrderService orderService;
    @GetMapping("conditionSearch")
    @ApiOperation(value = "订单搜索")
    public Result<PageResult>orderQuery(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单查询参数:{}",ordersPageQueryDTO);
        PageResult pageResult = orderService.pageQuery(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("details/{id}")
    @ApiOperation(value = "查询订单详情")
    public Result<OrderVO>orderDetail(@PathVariable  String id){
        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }

    @PutMapping("cancel")
    @ApiOperation(value = "取消订单")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO){//TODO 哪里用到了？
        orderService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }

    @PutMapping("confirm")
    @ApiOperation(value = "接单")
    public Result takeOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO){ //TODO 这里用Long id接收不到 Q：前端请求payLoad只有一个参数使用该注解接收不到吗？
        orderService.takeOrder(ordersConfirmDTO);
        return Result.success();
    }

    @PutMapping("rejection")
    @ApiOperation(value = "拒单")
    public Result rejectOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        orderService.rejectOrder(ordersRejectionDTO);
        return Result.success();
    }

    @PutMapping("delivery/{id}")
    @ApiOperation(value = "订单派送")
    public Result deliveryOrder(@PathVariable String id){
        orderService.deliveryOrder(id);
        return Result.success();
    }

    @PutMapping("complete/{id}")
    @ApiOperation(value = "完成订单")
    public Result completeOrder(@PathVariable String id){
        orderService.completeOrder(id);
        return Result.success();
    }

    @GetMapping("statistics")
    @ApiOperation(value = "各个状态的订单数量统计")
    public Result<OrderStatisticsVO>statistics(){
        OrderStatisticsVO orderStatisticsVO=orderService.statistics();
        return Result.success(orderStatisticsVO);

    }
}
