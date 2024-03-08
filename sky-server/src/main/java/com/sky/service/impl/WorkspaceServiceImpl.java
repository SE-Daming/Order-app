package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    OrdersMapper ordersMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    DishMapper dishMapper;

    /**
     * 查询今天的运营数据
     */
    @Override
    public BusinessDataVO todayData(LocalDate localDate) {
        Double turnover=ordersMapper.getAmountToday(localDate);//今日营业额
        turnover=(turnover==null?0:turnover);

        Integer validOrderCount=ordersMapper.getValidOrderToday(localDate);//今日有效订单数
        validOrderCount=(validOrderCount==null?0:validOrderCount);

        Double totalOrderCount=ordersMapper.getNumOfOrdersToday(localDate); //今日总订单数
        totalOrderCount=(totalOrderCount==null?0:totalOrderCount);

        Double completedOrderCount=ordersMapper.getNumOfCompletedOrderToday(localDate);//今日完成订单数
        completedOrderCount=(completedOrderCount==null?0:completedOrderCount);

        Double orderCompletionRate=0.0;//订单完成率,暂时计算为已完成订单除以总订单
        if(totalOrderCount!=0){
            orderCompletionRate=completedOrderCount/totalOrderCount;
        }

        Double unitPrice=0.0;//平均客单价、暂时计算为营业额除以有效订单数
        if(validOrderCount!=0){
            unitPrice=turnover/validOrderCount;
        }
        Integer newUsers=Integer.valueOf(userMapper.userStatisticsNew(localDate));//新增用户数
        BusinessDataVO businessDataVO=BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
        return businessDataVO;
    }

    /**
     * 套餐总览
     */
    @Override
    public SetmealOverViewVO setmealOverview() {
        Integer discontinued=setmealMapper.getNumOfStatus(StatusConstant.DISABLE);//停售套餐数量
        discontinued=(discontinued==null?0:discontinued);
        Integer sold=setmealMapper.getNumOfStatus(StatusConstant.ENABLE);//启售套餐数量
        sold=(sold==null?0:sold);
        SetmealOverViewVO setmealOverViewVO=SetmealOverViewVO.builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
        return setmealOverViewVO;
    }

    /**
     * 菜品总览
     */
    @Override
    public DishOverViewVO dishOverview() {
        Integer discontinued=dishMapper.getNumOfStatus(StatusConstant.DISABLE);
        discontinued=(discontinued==null?0:discontinued);

        Integer sold=dishMapper.getNumOfStatus(StatusConstant.ENABLE);
        sold=(sold==null?0:sold);
        DishOverViewVO dishOverViewVO=DishOverViewVO.builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
        return dishOverViewVO;
    }

    /**
     * 订单数据管理
     */
    @Override
    public OrderOverViewVO orderOverview() {
        Integer waitingOrders=ordersMapper.getNumOfStatus(2);
        waitingOrders=(waitingOrders==null?0:waitingOrders);

        Integer deliveredOrders=ordersMapper.getNumOfStatus(3);
        deliveredOrders=(deliveredOrders==null?0:deliveredOrders);

        Integer completedOrders=ordersMapper.getNumOfStatus(5);
        completedOrders=(completedOrders==null?0:completedOrders);

        Integer cancelledOrders=ordersMapper.getNumOfStatus(6);
        cancelledOrders=(cancelledOrders==null?0:cancelledOrders);

        Integer allOrders=ordersMapper.getNumOfStatus(null);
        allOrders=(allOrders==null?0:allOrders);

        OrderOverViewVO orderOverViewVO= OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
        log.info("订单数据:{}",orderOverViewVO);
        return orderOverViewVO;
    }
}
