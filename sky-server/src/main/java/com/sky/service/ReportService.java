package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

public interface ReportService {
    OrderReportVO orderStatistics(String begin, String end);

    TurnoverReportVO turnoverStatistics(String begin, String end);

    UserReportVO userStatistics(String begin, String end);

    SalesTop10ReportVO saleStatistics(String begin, String end);
}
