package com.sky.service;

import com.sky.vo.BusinessDataVO;

import java.time.LocalDate;

public interface WorkspaceService {
    BusinessDataVO todayData(LocalDate localDate);
}
