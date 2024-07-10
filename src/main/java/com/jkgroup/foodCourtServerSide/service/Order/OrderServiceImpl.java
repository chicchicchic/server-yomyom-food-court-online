package com.jkgroup.foodCourtServerSide.service.Order;

import com.jkgroup.foodCourtServerSide.dto.Order.OrderDTO;
import com.jkgroup.foodCourtServerSide.dto.OrderManagement.OrderItemManagementDTO;
import com.jkgroup.foodCourtServerSide.dto.OrderManagement.OrderManagementDTO;
import com.jkgroup.foodCourtServerSide.enums.OrderItemStatusEnum;
import com.jkgroup.foodCourtServerSide.enums.OrderStatusEnum;
import com.jkgroup.foodCourtServerSide.exception.ValidationException;
import com.jkgroup.foodCourtServerSide.model.Dish;
import com.jkgroup.foodCourtServerSide.model.Order;
import com.jkgroup.foodCourtServerSide.model.OrderItem;
import com.jkgroup.foodCourtServerSide.repository.Dish.DishRepository;
import com.jkgroup.foodCourtServerSide.repository.Order.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;

    // [GET]
    @Override
    public List<OrderItem> getOrderItemsByStatusAndUserId(OrderStatusEnum orderStatus, int userId) {
        List<Order> orders = orderRepository.findByOrderStatusEnumAndUserId(orderStatus, userId);

        // Extract and filter orderItems
        List<OrderItem> orderItems = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .filter(orderItem ->
                                !orderItem.getOrderItemStatusEnum().equals(OrderItemStatusEnum.COMPLETED)
//                        && !orderItem.getOrderItemStatusEnum().equals(OrderItemStatusEnum.RETURNED)
                )
                .collect(Collectors.toList());

        return orderItems;
    }

    @Override
    public Page<Dish> getDishesByUserIdAndStatus(int userId, OrderItemStatusEnum status, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // Fetch all orders for the given user ID
        List<Order> orders = orderRepository.findByUserId(userId);

        // Initialize a set to collect unique dish IDs
        Set<Integer> dishIds = new HashSet<>();

        // Iterate over each order to collect dish IDs with the specified status
        for (Order order : orders) {
            List<OrderItem> orderItems = order.getOrderItems().stream()
                    .filter(item -> item.getOrderItemStatusEnum() == status)
                    .collect(Collectors.toList());

            // Add dish IDs from order items to the set
            dishIds.addAll(orderItems.stream()
                    .map(OrderItem::getDishId)
                    .collect(Collectors.toList()));
        }

        // Fetch dishes by their IDs
        Page<Dish> dishes = dishRepository.findAllByDishIdIn(dishIds, pageable);

        return dishes;
    }

    // Get All Orders
    @Override
    public Page<OrderManagementDTO> getAllOrders(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Order> ordersPage = orderRepository.findByIsDeletedFalse(pageable);

        return ordersPage.map(order -> {
            List<OrderItemManagementDTO> orderItemDTOs = order.getOrderItems().stream()
                    .map(orderItem -> {
                        Dish dish = dishRepository.findById(orderItem.getDishId()).orElse(null);
                        String dishImageBase64 = (dish != null && dish.getImage() != null)
                                ? Base64.getEncoder().encodeToString(dish.getImage())
                                : null;
                        String dishName = (dish != null) ? dish.getName() : null;

                        OrderItemManagementDTO orderItemManagementDTO = new OrderItemManagementDTO();
                        orderItemManagementDTO.setOrderItemId(orderItem.getOrderItemId());
                        orderItemManagementDTO.setQuantity(orderItem.getQuantity());
                        orderItemManagementDTO.setTotalPrice(orderItem.getTotalPrice());
                        orderItemManagementDTO.setOrderItemStatusEnum(orderItem.getOrderItemStatusEnum());
                        orderItemManagementDTO.setDishName(dishName);
                        orderItemManagementDTO.setDishImage(dishImageBase64);
                        orderItemManagementDTO.setCreatedAt(orderItem.getCreatedAt());
                        orderItemManagementDTO.setCreatedBy(orderItem.getCreatedBy());
                        orderItemManagementDTO.setUpdatedBy(orderItem.getUpdatedBy());
                        orderItemManagementDTO.setDeleted(orderItem.isDeleted());

                        return orderItemManagementDTO;
                    })
                    .collect(Collectors.toList());

            OrderManagementDTO orderManagementDTO = new OrderManagementDTO();
            orderManagementDTO.setOrderId(order.getOrderId());
            orderManagementDTO.setAddress(order.getAddress());
            orderManagementDTO.setPhone(order.getPhone());
            orderManagementDTO.setTotalPayment(order.getTotalPayment());
            orderManagementDTO.setPaymentMethod(order.getPaymentMethod());
            orderManagementDTO.setDeliveryTime(order.getDeliveryTime());
            orderManagementDTO.setNotes(order.getNotes());
            orderManagementDTO.setOrderStatusEnum(order.getOrderStatusEnum());
            orderManagementDTO.setOrderItems(orderItemDTOs);
            orderManagementDTO.setCreatedAt(order.getCreatedAt());
            orderManagementDTO.setCreatedBy(order.getCreatedBy());
            orderManagementDTO.setUpdatedBy(order.getUpdatedBy());
            orderManagementDTO.setDeleted(order.isDeleted());

            return orderManagementDTO;
        });
    }

    // Get All Orders Trash
    @Override
    public Page<OrderManagementDTO> getAllOrdersTrash(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Order> ordersPage = orderRepository.findByIsDeletedTrue(pageable);

        return ordersPage.map(order -> {
            List<OrderItemManagementDTO> orderItemDTOs = order.getOrderItems().stream()
                    .map(orderItem -> {
                        Dish dish = dishRepository.findById(orderItem.getDishId()).orElse(null);
                        String dishImageBase64 = (dish != null && dish.getImage() != null)
                                ? Base64.getEncoder().encodeToString(dish.getImage())
                                : null;
                        String dishName = (dish != null) ? dish.getName() : null;

                        OrderItemManagementDTO orderItemManagementDTO = new OrderItemManagementDTO();
                        orderItemManagementDTO.setOrderItemId(orderItem.getOrderItemId());
                        orderItemManagementDTO.setQuantity(orderItem.getQuantity());
                        orderItemManagementDTO.setTotalPrice(orderItem.getTotalPrice());
                        orderItemManagementDTO.setOrderItemStatusEnum(orderItem.getOrderItemStatusEnum());
                        orderItemManagementDTO.setDishName(dishName);
                        orderItemManagementDTO.setDishImage(dishImageBase64);
                        orderItemManagementDTO.setCreatedAt(orderItem.getCreatedAt());
                        orderItemManagementDTO.setCreatedBy(orderItem.getCreatedBy());
                        orderItemManagementDTO.setUpdatedBy(orderItem.getUpdatedBy());
                        orderItemManagementDTO.setDeleted(orderItem.isDeleted());

                        return orderItemManagementDTO;
                    })
                    .collect(Collectors.toList());

            OrderManagementDTO orderManagementDTO = new OrderManagementDTO();
            orderManagementDTO.setOrderId(order.getOrderId());
            orderManagementDTO.setAddress(order.getAddress());
            orderManagementDTO.setPhone(order.getPhone());
            orderManagementDTO.setTotalPayment(order.getTotalPayment());
            orderManagementDTO.setPaymentMethod(order.getPaymentMethod());
            orderManagementDTO.setDeliveryTime(order.getDeliveryTime());
            orderManagementDTO.setNotes(order.getNotes());
            orderManagementDTO.setOrderStatusEnum(order.getOrderStatusEnum());
            orderManagementDTO.setOrderItems(orderItemDTOs);
            orderManagementDTO.setCreatedAt(order.getCreatedAt());
            orderManagementDTO.setCreatedBy(order.getCreatedBy());
            orderManagementDTO.setUpdatedBy(order.getUpdatedBy());
            orderManagementDTO.setDeleted(order.isDeleted());

            return orderManagementDTO;
        });
    }


    // [POST]
    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        try {
            Order order = new Order();
            order.setUserId(orderDTO.getUserId());
            order.setAddress(orderDTO.getAddress());
            order.setPhone(orderDTO.getPhone());
            order.setTotalPayment(orderDTO.getTotalPayment());
            order.setPaymentMethod(orderDTO.getPaymentMethod());
            order.setDeliveryTime(orderDTO.getDeliveryTime());
            order.setNotes(orderDTO.getNotes());
            order.setOrderStatusEnum(OrderStatusEnum.ACTIVE);

            LocalDateTime now = LocalDateTime.now();
            order.setCreatedAt(now);
            order.setCreatedBy(orderDTO.getCreatedBy());
            order.setUpdatedBy(orderDTO.getUpdatedBy());
            order.setDeleted(false);

            order.setOrderItems(orderDTO.getOrderItems().stream().map(orderItemDTO -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setDishId(orderItemDTO.getDishId());
                orderItem.setQuantity(orderItemDTO.getQuantity());
                orderItem.setTotalPrice(orderItemDTO.getTotalPrice());
                orderItem.setOrderItemStatusEnum(OrderItemStatusEnum.ORDERED);

                orderItem.setCreatedAt(now);
                orderItem.setCreatedBy(orderDTO.getCreatedBy());
                orderItem.setUpdatedBy(orderDTO.getUpdatedBy());
                orderItem.setDeleted(false);

                orderItem.setOrder(order); // set the relationship
                return orderItem;
            }).collect(Collectors.toList()));

            return orderRepository.save(order);
        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add("Error creating order: " + e.getMessage());

            throw new ValidationException("Validation failed", errors);
        }
    }


    // [PUT]
    @Override
    public ResponseEntity<?> updateOrderStatus(Long orderId, OrderStatusEnum newStatus, boolean isDeleted) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Order Found!");
        }

        Order orderFound = orderOptional.get();
        orderFound.setOrderStatusEnum(newStatus);
        orderFound.setDeleted(isDeleted);
        orderRepository.save(orderFound);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("The Order Is Completed!");
    }
}
