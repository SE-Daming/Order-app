package com.sky.service.impl;

import com.sky.mapper.OrdersMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
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
}
