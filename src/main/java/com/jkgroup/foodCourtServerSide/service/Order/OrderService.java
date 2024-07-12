package com.jkgroup.foodCourtServerSide.service.Order;

import com.jkgroup.foodCourtServerSide.dto.Order.OrderDTO;
import com.jkgroup.foodCourtServerSide.dto.OrderManagement.OrderManagementDTO;
import com.jkgroup.foodCourtServerSide.enums.OrderItemStatusEnum;
import com.jkgroup.foodCourtServerSide.enums.OrderStatusEnum;
import com.jkgroup.foodCourtServerSide.model.Dish;
import com.jkgroup.foodCourtServerSide.model.Order;
import com.jkgroup.foodCourtServerSide.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    // [GET]
    public List<OrderItem> getOrderItemsByStatusAndUserId(OrderStatusEnum orderStatus, int userId);
    public Page<Dish> getDishesByUserIdAndStatus(int userId, OrderItemStatusEnum status, int pageNumber, int pageSize);
    public Page<OrderManagementDTO> getAllOrders(int pageNumber, int pageSize);
    public Page<OrderManagementDTO> getAllOrdersTrash(int pageNumber, int pageSize);

    // [POST]
    Order createOrder(OrderDTO orderDTO);

    // [PUT]
    public ResponseEntity<?> updateOrderStatus(Long orderId, OrderStatusEnum newStatus, boolean isDeleted);
}

