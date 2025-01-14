package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.vo.OrderReportVO;
import com.sky.vo.OrderVO;
import com.sky.vo.TurnoverReportVO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
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
    @Update("update orders set status=2,checkout_time=#{time},pay_method=#{ordersPaymentDTO.payMethod} where number=#{ordersPaymentDTO.orderNumber}")
    void setStatus2(OrdersPaymentDTO ordersPaymentDTO,LocalDateTime time);

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

    /**
     * 封装日期和每日订单
     * @param begin
     * @param end
     * @return
     */
    List<OrderReportVO> orderStatistics1(String begin, String end);

    /**
     * 封装日期和每日有效订单
     * 设状态为6、7的为无效订单
     * @param begin
     * @param end
     * @return
     */
    List<OrderReportVO> orderStatistics2(String begin, String end);

    /**
     * 订单总数
     * @param begin
     * @param end
     * @return
     */
    Integer orderStatistics3(String begin, String end);

    /**
     * 有效订单总数
     * @param begin
     * @param end
     * @return
     */
    Integer orderStatistics4(String begin, String end);

    /**
     * 找出时间段内状态为已完成的订单
     * @param begin
     * @param end
     * @return
     */
    List<TurnoverReportVO> getAmountBetweenTime(String begin, String end);

    /**
     * 时间范围内新增用户的id
     * @param begin
     * @param end
     * @return
     */
    List<Long> getIdBetweenTime(String begin, String end);

    /**
     * 当天的的营业额
     * @param localDate
     * @return
     */
    Double getAmountToday(LocalDate localDate);

    /**
     * 当天的有效订单数(有效订单暂定为状态不为6.7)
     * @param localDate
     * @return
     */
    Integer getValidOrderToday(LocalDate localDate);

    /**
     *当天的总订单数
     * @param localDate
     * @return
     */
    Double getNumOfOrdersToday(LocalDate localDate);


    /**
     * 当天完成订单数
     * @param localDate
     * @return
     */
    Double getNumOfCompletedOrderToday(LocalDate localDate);

    Integer getNumOfStatus(Integer status);



    /**
     * 找出超过15分钟未支付的订单id
     */
    @Select("select id from orders where status=1 and order_time < now()-interval 15 minute")
    List<Long> timingCancel();

    /**
     * 根据id删除订单
     * @param id
     */
    @Delete("delete from orders where id=#{id}")
    void removeById(Long id);
}
