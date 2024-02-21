package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工
     */
    @Insert("INSERT INTO employee(username, name, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "VALUES (#{username}, #{name}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void addEmployee(Employee employee);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */

    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 修改员工账号状态
     * @param status
     * @param id
     */
    @Update("update employee set status=#{status} where id=#{id}")
    void changeStatus(int status, Long id);

    /**
     * 修改员工信息
     * @param employee
     */
    @Update("update employee set username=#{username},name=#{name},phone=#{phone},sex=#{sex},id_number=#{idNumber},update_time=#{updateTime} where id=#{id}")
    @AutoFill(value = OperationType.UPDATE)
    void mofifyEmployee(Employee employee);

    /**+
     * 按id查询员工
     * @param id
     * @return
     */
    @Select("select * from employee where id=#{id}")
    EmployeeDTO selectById(long id);

    /**
     * 修改员工密码
     * @param passwordEditDTO
     */
    @Update("update employee set password=#{newPassword} where id=#{empId} and password=#{oldPassword}")
    void mofifyPassword(PasswordEditDTO passwordEditDTO);
}
