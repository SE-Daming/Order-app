package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //需要进行md5加密，然后再进行比对
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * Q1、员工的id 状态、密码如何设置
     */
    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        employee.setStatus(StatusConstant.ENABLE);

//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());

        String password= PasswordConstant.DEFAULT_PASSWORD;
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        employee.setPassword(password);

        log.info("employee:{}",employee);
        employeeMapper.addEmployee(employee);
    }

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());

        Page<Employee>page=employeeMapper.pageQuery(employeePageQueryDTO);

        long total=page.getTotal();

        List<Employee>records=page.getResult();

        return new PageResult(total,records);

    }

    /**
     * 修改员工账号状态
     * @param status
     * @param id
     */

    @Override
    public void changeStatus(int status, Long id) {
        employeeMapper.changeStatus(status,id);
    }

    /**
     * 根据id回显员工数据
     * @param id
     * @return
     */

    @Override
    public EmployeeDTO selectById(long id) {
        return employeeMapper.selectById(id);
    }

    /**
     * 修改员工信息
     * @param employeeDTO
     */

    @Override
    public void modifyEmployee(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
//        LocalDateTime updateTime=LocalDateTime.now();
//        employee.setUpdateTime(updateTime);
        employeeMapper.mofifyEmployee(employee);
    }

    /**
     * 修改员工密码
     */
    @Override
    public void modifyPassword(PasswordEditDTO passwordEditDTO) {
        String newPassword=passwordEditDTO.getNewPassword();
        newPassword=DigestUtils.md5DigestAsHex(newPassword.getBytes());
        passwordEditDTO.setNewPassword(newPassword);

        String oldPassword=passwordEditDTO.getOldPassword();
        oldPassword=DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        passwordEditDTO.setOldPassword(oldPassword);
        employeeMapper.mofifyPassword(passwordEditDTO);
    }
}
