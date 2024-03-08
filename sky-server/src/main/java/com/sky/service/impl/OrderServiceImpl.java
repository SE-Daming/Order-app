package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrdersMapper ordersMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Autowired
    ShoppingCartMapper shoppingCartMapper;
    @Autowired
    AddressMapper addressMapper;
    @Autowired
    UserMapper userMapper;

    /**
     * 新增订单
     * 插入order表和order-detail表
     */
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //要插入订单表的orders
        Orders orders=new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);

        //查出订单地址信息用来赋值给orders
        AddressBook addressBook=addressMapper.getById(ordersSubmitDTO.getAddressBookId());

        String address=addressBook.getProvinceName()+addressBook.getCityName()+addressBook.getDistrictName()+addressBook.getDetail();//地址信息
        orders.setAddress(address);
        orders.setUserId(BaseContext.getCurrentId());
        orders.setPhone(addressBook.getPhone());

        String userName=userMapper.getUserNameById(BaseContext.getCurrentId());
        orders.setUserName(userName);
        orders.setConsignee(addressBook.getConsignee());
        orders.setOrderTime(LocalDateTime.now());//下单时间设为此刻

//TODO:订单号如何解决? A:订单号随机生成并通过订单号获取订单id返回vo
        String orderNumber=String.valueOf(UUID.randomUUID());
        orders.setNumber(orderNumber);
        orders.setStatus(1);
        orders.setPayStatus(0);
        orders.setDeliveryStatus(1);

        //保存订单
        ordersMapper.save(orders);

        //创建返回对象
        Long orderId=ordersMapper.getIdByNumber(orderNumber);//订单id
        OrderSubmitVO orderSubmitVO=OrderSubmitVO.builder()
                .id(orderId)
                .orderNumber(orderNumber)
                .orderAmount(orders.getAmount())
                .orderTime(LocalDateTime.now())
                .build();

        //订单明细
        List<ShoppingCart> shoppingCarts=shoppingCartMapper.getByUserId(BaseContext.getCurrentId());
        shoppingCarts.stream().forEach(shoppingCart->{

//            创建订单明细
            OrderDetail orderDetail=new OrderDetail();
            BeanUtils.copyProperties(shoppingCart,orderDetail);
            orderDetail.setOrderId(orderId);

            //插入订单明细表
            orderDetailMapper.save(orderDetail);
        });

        return orderSubmitVO;
    }

    /**
     * 订单分页查询
     */
//    @Override
//    public PageResult pageQuery(int page, int pageSize, Integer status) {
//
//        //订单分页查询
//        PageHelper.startPage(page,pageSize);
//        Page<OrderVO>ordersPage=ordersMapper.pageQuery(status);
//        long total = ordersPage.getTotal();
//        List<OrderVO> result = ordersPage.getResult();
//
////        订单明细查询
//        result = result.stream()
//                .map(orderVO -> {
//                    List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderVO.getId());
//                    orderVO.setOrderDetailList(orderDetails);
//
//                    orderVO.setOrderDishes("123");
//                    return orderVO;
//                })
//                .collect(Collectors.toList());
//        return new PageResult(total,result);
//    }
    @Override
    public PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {

        //订单分页查询
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<OrderVO>ordersPage=ordersMapper.pageQuery(ordersPageQueryDTO);
        long total = ordersPage.getTotal();
        List<OrderVO> result = ordersPage.getResult();

//        订单明细查询
        result = result.stream()
                .map(orderVO -> {
                    List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderVO.getId());
                    orderVO.setOrderDetailList(orderDetails);

                    orderVO.setOrderDishes("123");//TODO :这个属性如何设置
                    return orderVO;
                })
                .collect(Collectors.toList());
        return new PageResult(total,result);
    }

    /**
     * 用户端订单分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    public PageResult pageQuery4User(int pageNum, int pageSize, Integer status) {
        // 设置分页
        PageHelper.startPage(pageNum, pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        // 分页条件查询
        Page<Orders> page = ordersMapper.pageQuery2(ordersPageQueryDTO);

        List<OrderVO> list = new ArrayList();

        // 查询出订单明细，并封装入OrderVO进行响应
        if (page != null && page.getTotal() > 0) {
            for (Orders orders : page) {
                Long orderId = orders.getId();// 订单id

                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(), list);
    }

    /**
     * 根据订单id查询订单
     */
    @Override
    public OrderVO getById(String id) {
        Orders orders=ordersMapper.getById(id);
        OrderVO orderVO=new OrderVO();
        BeanUtils.copyProperties(orders,orderVO);
        String orderDishes="123";
        orderVO.setOrderDishes(orderDishes); //TODO:订单菜品如何设置
        List<OrderDetail>orderDetailList=new ArrayList<>();
        orderDetailList=ordersMapper.getOrderDetailByOrderId(id);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    /**
     * 取消订单
     * 把order的status设为6、订单原因填充
     * 把orderDetail的记录删除
     */
    @Override
    public void cancelOrder(OrdersCancelDTO ordersCancelDTO) {
        String id=String.valueOf(ordersCancelDTO.getId());
        ordersMapper.cancelOrder(ordersCancelDTO);
        orderDetailMapper.deleteByOrderId(id);
    }

    /**
     * 订单支付
     * 把订单状态改为2、购物车清空
     * 结账时间填充
     */
    @Override
    public OrderPaymentVO orderPay(OrdersPaymentDTO ordersPaymentDTO) {
        ordersMapper.setStatus2(ordersPaymentDTO,LocalDateTime.now());
        shoppingCartMapper.clean();
        return null;
    }

    /**
     * 接单
     * 把订单状态改为3
     */
    @Override
    public void takeOrder(OrdersConfirmDTO ordersConfirmDTO) {
        ordersMapper.setStatus3(ordersConfirmDTO.getId());
    }

    /**
     * 拒单功能
     * 把状态改为已取消、拒单原因填充
     */
    @Override
    public void rejectOrder(OrdersRejectionDTO ordersRejectionDTO) {
        ordersMapper.rejectOrder(ordersRejectionDTO);
    }

    /**
     * 再来一单、操作订单表和订单详情表
     * 根据订单的id查询订单并创建插入新的数据
     */
    @Override
    public void repetition(String id) {

        //创建订单对象
        Orders orders=ordersMapper.getById(id);
        orders.setId(null);
        String orderNumber=String.valueOf(UUID.randomUUID());
        orders.setNumber(orderNumber);

        //新增订单
        ordersMapper.save(orders);

        //得到订单id=》赋值给订单明细对象 TODO 如何获得新插入的订单的id?暂时用订单号获得
        Long orderId=ordersMapper.getIdByNumber(orders.getNumber());

        //创建订单明细对象
        List<OrderDetail>orderDetails=orderDetailMapper.getByOrderId(Long.valueOf(id));
        orderDetails.forEach(orderDetail -> {
            orderDetail.setId(null);
            orderDetail.setOrderId(orderId);

            //新增订单明细
            orderDetailMapper.save(orderDetail);
        });

    }

    /**
     * 订单派送
     * 将订单的状态更新为4
     */
    @Override
    public void deliveryOrder(String id) {
        ordersMapper.setStatus4(id);
    }

    /**
     * 订单完成
     * 将订单的状态更新为5
     */
    @Override
    public void completeOrder(String id) {
        ordersMapper.setStatus5(id);
    }

    /**
     * 统计各个状态的数量
     */
    @Override
    public OrderStatisticsVO statistics() {
        Integer toBeConfirmed=ordersMapper.getTotalStatus2();
        Integer confirmed=ordersMapper.getTotalStatus3();
        Integer deliveryInProgress=ordersMapper.getTotalStatus4();

        OrderStatisticsVO orderStatisticsVO=new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);

        return orderStatisticsVO;
    }
}
