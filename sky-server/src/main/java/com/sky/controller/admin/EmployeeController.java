package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工管理")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     */
    @PostMapping
    @ApiOperation(value = "新增员工")
    public Result addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("employeeDto:{}", employeeDTO);
        employeeService.addEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询
     */
    @GetMapping("page")
    @ApiOperation(value = "员工分页查询")
    public Result<PageResult> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("dto:{}", employeePageQueryDTO);
        return Result.success(employeeService.pageQuery(employeePageQueryDTO));
    }

    /**
     * 启用、禁用员工账号
     */
    @PostMapping("status/{status}")
    @ApiOperation(value = "启用禁用员工账号")
    public Result changeStatus(@PathVariable int status, Long id) {
        employeeService.changeStatus(status, id);
        return Result.success();
    }

    /**
     * 修改员工时回显数据,即按id查询员工
     */
    @GetMapping("{id}")
    @ApiOperation(value = "按id查询员工信息")
    public Result<EmployeeDTO>echo(@PathVariable long id){
        return Result.success(employeeService.selectById(id));
    }

    /**
     * 修改员工信息
     */
    @PutMapping
    @ApiOperation(value="修改员工信息")
    public Result modifyEmployee(@RequestBody EmployeeDTO employeeDTO){
        employeeService.modifyEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("editPassword")
    @ApiOperation(value = "修改员工密码")
    public  Result modifyPassword(@RequestBody PasswordEditDTO passwordEditDTO){
        employeeService.modifyPassword(passwordEditDTO);
        return Result.success();
    }
}