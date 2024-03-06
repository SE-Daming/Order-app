package com.sky.service;

import com.sky.vo.OrderReportVO;

public interface ReportService {
    OrderReportVO orderStatistics(String begin, String end);
}
