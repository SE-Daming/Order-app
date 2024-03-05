package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressMapper {
    /**
     * 插入新地址
     * @param addressBook
     */
    @Insert("insert into address_book(user_id,consignee,sex,phone,province_code,province_name,city_code,city_name,district_code,district_name,detail,label,is_default)values " +
            "(#{userId},#{consignee},#{sex},#{phone},#{provinceCode},#{provinceName},#{cityCode},#{cityName},#{districtCode},#{districtName},#{detail},#{label},#{isDefault})")
    void save(AddressBook addressBook);

    /**
     * 根据userId查询地址
     * @param userId
     * @return
     */
    @Select("select * from address_book where user_id=#{userId}")
    List<AddressBook> getAllByUserId(Long userId);

    /**
     * 修改地址
     * @param addressBook
     */
    @Update("update address_book set consignee=#{consignee},sex=#{sex},phone=#{phone},province_code=#{provinceCode},province_name=#{provinceName}" +
            ",city_code=#{cityCode},city_name=#{cityName},district_code=#{districtCode},district_name=#{districtName},detail=#{detail}" +
            ",label=#{label},is_default=#{isDefault} where user_id=#{userId}")
    void update(AddressBook addressBook);

    /**
     * 根据id删除记录
     * @param id
     */
    @Delete("delete from address_book where id=#{id}")
    void deleteById(Long id);

    /**
     * 根据id查询记录
     * @param id
     * @return
     */
    @Select("select * from address_book where id=#{id}")
    AddressBook getById(Long id);

    /**
     * 把id处记录默认值设为1
     * @param id
     */
    @Update("update address_book set is_default=1 where id=#{id}")
    void setDefault(Long id);

    /**
     * 把 userId的所有default设为0
     * @param userId
     */
    @Update("update address_book set is_default=0 where user_id=#{userId}")
    void setNotDefault(Long userId);

    /**
     * 查询默认地址
     * @param userId
     * @return
     */
    @Select("select * from address_book where user_id=#{userId} and is_default=1")
    AddressBook getDefaultAddressByUserId(Long userId);
}












