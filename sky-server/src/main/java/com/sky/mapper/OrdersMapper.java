package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrdersMapper {
    /**
     * 新增订单
     * @param orders
     */
    @Insert("INSERT INTO orders (number, status, user_id, address_book_id, order_time, checkout_time, pay_method, pay_status, amount, remark, phone, address, user_name, consignee, cancel_reason, rejection_reason, cancel_time, estimated_delivery_time, delivery_status, delivery_time, pack_amount, tableware_number, tableware_status)\n" +
            "    VALUES (#{number}, #{status}, #{userId}, #{addressBookId}, #{orderTime}, #{checkoutTime}, #{payMethod}, #{payStatus}, #{amount}, #{remark}, #{phone}, #{address}, #{userName}, #{consignee}, #{cancelReason}, #{rejectionReason}, #{cancelTime}, #{estimatedDeliveryTime}, #{deliveryStatus}, #{deliveryTime}, #{packAmount}, #{tablewareNumber}, #{tablewareStatus})")

    void save(Orders orders);

    /**
     * 按订单号查询订单id
     * @param number
     * @return
     */
    @Select("select id from orders where number=#{number}")
    Long getIdByNumber(String number);

    /**
     * 分页查询、status可能是null
     * @param
     * @return
     */
    Page<OrderVO> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    Page<Orders> pageQuery2(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单id查询订单
     * @param id
     * @return
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(String id);

    /**
     * 按订单id查询订单详情
     * @param id
     * @return
     */
    @Select("select * from order_detail where order_id=#{id}")
    List<OrderDetail> getOrderDetailByOrderId(String id);



    /**
     * 订单状态改为待接单
     * @param ordersPaymentDTO
     */
    @Update("update orders set status=2 where number=#{orderNumber}")
    void setStatus2(OrdersPaymentDTO ordersPaymentDTO);

    /**
     * 取消订单、把订单状态设为6、订单原因若不为空则填充
     * 订单取消时间
     * @param ordersCancelDTO
     */
    void cancelOrder(OrdersCancelDTO ordersCancelDTO);

    /**
     * 订单状态改为已接单
     * @param id
     */
    @Update("update orders set status=3 where id=#{id}")
    void setStatus3(Long id);

    void rejectOrder(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 订单状态改为配送中
     * @param id
     */
    @Update("update orders set status=4 where id=#{id}")
    void setStatus4(String id);

    /**
     * 订单状态改为完成
     * @param id
     */
    @Update("update orders set status=5 where id=#{id}")
    void setStatus5(String id);

    /**
     * 统计待接单总数
     * @return
     */
    @Select("select count(*) from orders where status=2")
    Integer getTotalStatus2();

    /**
     * 统计已接单总数
     * @return
     */
    @Select("select count(*) from orders where status=3")
    Integer getTotalStatus3();

    /**
     * 统计派送中总数
     * @return
     */
    @Select("select count(*) from orders where status=4")
    Integer getTotalStatus4();
}
