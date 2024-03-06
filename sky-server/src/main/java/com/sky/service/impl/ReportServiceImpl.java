package com.sky.service.impl;

import com.sky.mapper.OrdersMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    OrdersMapper ordersMapper;

    /**
     * 订单统计
     */
    @Override
    public OrderReportVO orderStatistics(String begin, String end) {
        List<OrderReportVO> orderReportVO1=ordersMapper.orderStatistics1(begin,end);//封装日期、每日订单（因为有多条记录所以list）
        List<OrderReportVO> orderReportVO2=ordersMapper.orderStatistics2(begin,end);//封装日期、每日有效订单
        Integer totalOrderCount=ordersMapper.orderStatistics3(begin,end);//订单总数
        Integer validOrderCount=ordersMapper.orderStatistics4(begin,end);//有效订单总数

        //得到每一个对象的日期并拼接
        StringBuilder dateList=new StringBuilder();
       for(int i=0;i<orderReportVO1.size();i++){
           if(i==0){
               dateList.append(orderReportVO1.get(0).getDateList());
           }
           else{
               dateList.append(",").append(orderReportVO1.get(i).getDateList());
           }
       }

        //得到每一个对象的每日订单并拼接
        StringBuilder orderCountList=new StringBuilder();
        for(int i=0;i<orderReportVO1.size();i++){
            if(i==0){
                orderCountList.append(orderReportVO1.get(0).getOrderCountList());
            }
            else{
                orderCountList.append(",").append(orderReportVO1.get(i).getOrderCountList());
            }
        }

        //得到每一个对象的每日有效订单并拼接
        StringBuilder validOrderCountList=new StringBuilder();
        for(int i=0;i<orderReportVO1.size();i++){
            if(i==0){
                validOrderCountList.append(orderReportVO1.get(0).getValidOrderCountList());
            }
            else{
                validOrderCountList.append(",").append(orderReportVO1.get(i).getValidOrderCountList());
            }
        }

        Double orderCompletionRate=(double)validOrderCount/totalOrderCount;

        OrderReportVO orderReportVO=OrderReportVO.builder()
                .dateList(String.valueOf(dateList))
                .orderCountList(String.valueOf(orderCountList))
                .validOrderCountList(String.valueOf(validOrderCountList))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
        return orderReportVO;
    }
}
