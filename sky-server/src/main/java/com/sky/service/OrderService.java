package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    PageResult pageQuery4User(int page, int pageSize, Integer status);

    OrderVO getById(String id);

    void cancelOrder(OrdersCancelDTO ordersCancelDTO);

    OrderPaymentVO orderPay(OrdersPaymentDTO ordersPaymentDTO);

    void takeOrder(OrdersConfirmDTO ordersConfirmDTO);

    void rejectOrder(OrdersRejectionDTO ordersRejectionDTO);

    void repetition(String id);

    void deliveryOrder(String id);

    void completeOrder(String id);

    OrderStatisticsVO statistics();
}
