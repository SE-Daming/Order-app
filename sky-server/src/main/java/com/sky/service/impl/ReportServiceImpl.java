package com.sky.service.impl;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    OrderDetailMapper orderDetailMapper;

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

    /**
     * 用户统计
     * TODO 找出[begin,end]内每一天的总量和新增
     */
    @Override
    public UserReportVO userStatistics(String begin, String end) {
        log.info("begin :{},end:{}",begin,end);
        List<UserReportVO>userReportVOList=new ArrayList<>();//要返回的数据集合

        //遍历每一天并封装数据
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate beginDate = LocalDate.parse(begin, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);

        LocalDate currentDate = beginDate;
        while (!currentDate.isAfter(endDate)) {
            UserReportVO userReportVO=new UserReportVO();
            //统计截至总量
            userReportVO.setTotalUserList(userMapper.userStatisticsTotal(currentDate));
            userReportVO.setNewUserList(userMapper.userStatisticsNew(currentDate));
            userReportVO.setDateList(String.valueOf(currentDate));
            //加入集合
            userReportVOList.add(userReportVO);
            //日期向后一天
            currentDate = currentDate.plusDays(1);

        }
        //创建要返回的对象
        StringBuilder dateList=new StringBuilder();
        StringBuilder totalUserList=new StringBuilder();
        StringBuilder newUserList=new StringBuilder();
        for(int i=0;i< userReportVOList.size();i++){
            UserReportVO u=userReportVOList.get(i);
            if(i==0){
                dateList.append(u.getDateList());
                totalUserList.append(u.getTotalUserList());
                totalUserList.append(u.getNewUserList());
            }
            else{
                dateList.append(","+u.getDateList());
                totalUserList.append(","+u.getTotalUserList());
                totalUserList.append(","+u.getNewUserList());
            }
        }
        UserReportVO userReportVO=UserReportVO.builder()
                .dateList(String.valueOf(dateList))
                .totalUserList(String.valueOf(totalUserList))
                .newUserList(String.valueOf(newUserList))
                .build();

        return userReportVO;
    }

    /**
     * 销量top10统计
     * 1、找出时间范围内的订单
     * 2、找出订单明细的top 10
     */
    @Override
    public SalesTop10ReportVO saleStatistics(String begin, String end) {
        //找出符合条件的订单、应该是已完成状态
        List<Long>orderId=ordersMapper.getIdBetweenTime(begin,end);
        //找出明细集合
        List<SalesTop10ReportVO>salesTop10ReportVOList=orderDetailMapper.top10(orderId);
        StringBuilder nameList=new StringBuilder();
        StringBuilder numberList=new StringBuilder();
        for(int i=0;i<salesTop10ReportVOList.size();i++){
            SalesTop10ReportVO salesTop10ReportVO=salesTop10ReportVOList.get(i);
            if(i==0){
                nameList.append(salesTop10ReportVO.getNameList());
                numberList.append(salesTop10ReportVO.getNumberList());
            }
            else {
                nameList.append(","+salesTop10ReportVO.getNameList());
                numberList.append(","+salesTop10ReportVO.getNumberList());
            }
        }
        SalesTop10ReportVO salesTop10ReportVO=SalesTop10ReportVO.builder()
                .nameList(String.valueOf(nameList))
                .numberList(String.valueOf(numberList))
                .build();
        return salesTop10ReportVO;
    }
}
