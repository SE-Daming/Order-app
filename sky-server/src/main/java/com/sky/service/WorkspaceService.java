package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDate;

public interface WorkspaceService {
    BusinessDataVO todayData(LocalDate localDate);

    SetmealOverViewVO setmealOverview();
}
