package com.sky.task;

import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class orderTask {
    @Autowired
    OrdersMapper ordersMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;

    /**
     * 每隔一段时间查看订单看[待付款]的是否超过了15min、超过则自动取消
     * 删除订单记录和订单明细记录
     */
    @Scheduled(cron = "0/60 * * * * ?  ")
    public void task1(){
        log.info("正在定时清除订单......");

//        得到要删除的id
        List<Long>orderIds=ordersMapper.timingCancel();
        for(Long id:orderIds){
            ordersMapper.removeById(id);
            orderDetailMapper.deleteByOrderId(String.valueOf(id));
        }
    }
}
