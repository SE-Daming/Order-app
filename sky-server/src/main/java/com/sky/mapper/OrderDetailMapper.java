package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.vo.SalesTop10ReportVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /**
     * 新增订单明细
     * @param orderDetail
     */
    @Insert("INSERT INTO order_detail (name, image, order_id, dish_id, setmeal_id, dish_flavor, number, amount)\n" +
            "    VALUES (#{name}, #{image}, #{orderId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount})")


    void save(OrderDetail orderDetail);

    /**
     * 查询订单id的明细
     * @param id
     * @return
     */
    @Select("select * from order_detail where order_id=#{id}")
    List<OrderDetail> getByOrderId(Long id);

    /**
     * 根据订单id删除订单明细
     * @param id
     */
    @Delete("delete from order_detail where order_id=#{id}")
    void deleteByOrderId(String id);

//    @Select("select name as nameList,count(*)numberList from order_detail where order_id in #{orderId}  group by name" +
//            "order by numberList desc limit 10")
    List<SalesTop10ReportVO> top10(List<Long> orderId);
}