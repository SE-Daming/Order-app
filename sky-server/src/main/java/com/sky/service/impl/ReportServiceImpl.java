package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
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
            String s = orderReportVO1.get(i).getOrderCountList();
            if(i==0){
                orderCountList.append(s==null?0:s);
            }
            else{
                orderCountList.append(",").append(s==null?0:s);
            }
        }

        //得到每一个对象的每日有效订单并拼接
        StringBuilder validOrderCountList=new StringBuilder();
        for(int i=0;i<orderReportVO2.size();i++){
            String s = orderReportVO2.get(i).getValidOrderCountList();
            if(i==0){
                validOrderCountList.append(s==null?0:s);
            }
            else{
                validOrderCountList.append(",").append(s==null?0:s);
            }
        }

        Double orderCompletionRate=0.0;//订单完成率
        if(totalOrderCount!=0){
            orderCompletionRate=(double)validOrderCount/totalOrderCount;

        }

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

    /**
     * 营业额统计
     *
     */
    @Override
    public TurnoverReportVO turnoverStatistics(String begin, String end) {

        List<TurnoverReportVO>turnoverReportVOList=ordersMapper.getAmountBetweenTime(begin,end);
        StringBuilder dateList=new StringBuilder();
        StringBuilder turnoverList=new StringBuilder();

        for(int i=0;i<turnoverReportVOList.size();i++){
            String s=turnoverReportVOList.get(i).getDateList();
            String ts=turnoverReportVOList.get(i).getTurnoverList();
            if(i==0){
                dateList.append(s);
                turnoverList.append(ts==null?0:ts);
            }
            else {
                dateList.append(",").append(s);
                turnoverList.append(",").append(ts==null?0:ts);
            }
        }

        TurnoverReportVO turnoverReportVO=TurnoverReportVO.builder()
                .dateList(String.valueOf(dateList))
                .turnoverList(String.valueOf(turnoverList))
                .build();
        return turnoverReportVO;
    }


}
