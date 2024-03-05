package com.sky.controller.user;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "用户端订单管理")
@Slf4j
public class OrderController {
    @Autowired
    OrderService orderService;
    @PostMapping("submit")
    @ApiOperation(value = "用户下单")
    public Result<OrderSubmitVO>submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("DTO:{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO=orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    @GetMapping("historyOrders")
    @ApiOperation(value = "历史订单查询")
    public Result<PageResult>historyOrders(OrdersPageQueryDTO ordersPageQueryDTO){
        PageResult pageResult=orderService.pageQuery(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

//    @GetMapping("/historyOrders")
//    @ApiOperation("历史订单查询")
//    public Result<PageResult> page(int page, int pageSize, Integer status) {
//        PageResult pageResult = orderService.pageQuery4User(page, pageSize, status);//TODO：小程序正确获得结果但不显示. 时间的格式的问题
//        return Result.success(pageResult);
//    }

    @PutMapping("payment")
    @ApiOperation(value = "订单支付")
    public Result<OrderPaymentVO>orderPay(@RequestBody OrdersPaymentDTO ordersPaymentDTO){
        log.info("订单信息:{}",ordersPaymentDTO);

        OrderPaymentVO orderPaymentVO2=orderService.orderPay(ordersPaymentDTO);

        OrderPaymentVO orderPaymentVO = OrderPaymentVO.builder()//TODO：mock date
                .nonceStr("randomNonceStr")
                .paySign("samplePaySign")
                .timeStamp("sampleTimeStamp")
                .signType("sampleSignType")
                .packageStr("samplePackageStr")
                .build();

        return Result.success(orderPaymentVO);
    }

    @GetMapping("orderDetail/{id}")
    @ApiOperation(value = "查看订单详情")
    public Result<OrderVO>detailOrder(@PathVariable String id){
        log.info("id:{}",id);
        OrderVO orderVO=orderService.getById(id);
        return Result.success(orderVO);
    }

    @PostMapping("repetition/{id}")
    @ApiOperation(value = "再来一单")
    //TODO 按照美团外卖的写法、再来一单时跳转到购物车界面、购物车内商品更新为此单的菜品
    public Result  oneMoreOrder(@PathVariable String id){
        OrderVO orderVO = orderService.getById(id);
        OrdersSubmitDTO ordersSubmitDTO=new OrdersSubmitDTO();
//        TODO 再来一单的小程序页面流程？ WARN：调用时orders正常、订单明细表没有新增
//        A:再来一单调用submitOrder方法、新增明细表的逻辑是查询购物车内商品加入明细表、当点击再来一单时先clean购物车了
        BeanUtils.copyProperties(orderVO,ordersSubmitDTO);
//        orderService.submitOrder(ordersSubmitDTO);
        orderService.repetition(id);
        return Result.success();
    }

    @PutMapping("cancel/{id}")
    @ApiOperation(value = "取消订单")
    public Result cancelOrder(@PathVariable Long id){
        OrdersCancelDTO ordersCancelDTO=new OrdersCancelDTO();
        ordersCancelDTO.setId(id);
        orderService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }

    @GetMapping("reminder/{id}")
    @ApiOperation(value = "用户催单")
    public Result reminder(@PathVariable String id){
        return Result.success();
    }

}
