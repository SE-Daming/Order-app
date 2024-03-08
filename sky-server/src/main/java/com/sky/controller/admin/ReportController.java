package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/report")
@Api(tags = "数据统计")
public class ReportController {
    @Autowired
    ReportService reportService;
    @GetMapping("ordersStatistics")
    @ApiOperation(value = "订单统计")
    public Result<OrderReportVO>orderReport(String begin,String end){
        OrderReportVO orderReportVO=reportService.orderStatistics(begin,end);
        return Result.success(orderReportVO);
    }

    @GetMapping("turnoverStatistics")
    @ApiOperation(value = "营业额统计")
    public Result<TurnoverReportVO>turnoverReport(String begin,String end){
        TurnoverReportVO turnoverReportVO=reportService.turnoverStatistics(begin,end);
        return Result.success(turnoverReportVO);
    }

    @GetMapping("userStatistics")
    @ApiOperation(value = "用户统计接口")
    public Result<UserReportVO>userReport(String begin,String end){
        UserReportVO userReportVO=reportService.userStatistics(begin,end);
        return Result.success(userReportVO);
    }

    @GetMapping("top10")
    @ApiOperation(value = "销量top10")
    public Result<SalesTop10ReportVO>saleReport(String begin,String end){
        SalesTop10ReportVO salesTop10ReportVO=reportService.saleStatistics(begin,end);
        return Result.success(salesTop10ReportVO);
    }
}
