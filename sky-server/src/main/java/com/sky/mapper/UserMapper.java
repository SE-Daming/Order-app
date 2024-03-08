package com.sky.mapper;

import com.sky.entity.User;
import com.sky.vo.UserReportVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openId);

    @Insert("insert into user (openid, name, phone, sex, id_number, avatar, create_time)\n" +
            "        values (#{openid}, #{name}, #{phone}, #{sex}, #{idNumber}, #{avatar}, #{createTime})")
    void save(User user);

    /**
     * 根据id查用户名
     * @param userId
     * @return
     */
    @Select("select name from user where id=#{userId}")
    String getUserNameById(Long userId);


    /**
     * 封装日期和新增用户

     * @return
     */
    String userStatisticsTotal(LocalDate currentDate);

    /**
     * 当天新增人数
     * @param localDate
     * @return
     */
    String userStatisticsNew(LocalDate currentDate);


}
