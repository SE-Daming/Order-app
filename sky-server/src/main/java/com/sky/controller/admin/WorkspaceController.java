package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("admin/workspace")
@Api(tags = "工作台管理")
public class WorkspaceController {
    @Autowired
    WorkspaceService workspaceService;
    @GetMapping("businessData")
    @ApiOperation(value = "查询今日运营数据")
    public Result<BusinessDataVO>todayData(){
        LocalDate localDate=LocalDate.now();
        BusinessDataVO businessDataVO=workspaceService.todayData(localDate);
        return Result.success(businessDataVO);
    }
}
