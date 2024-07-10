package com.jkgroup.foodCourtServerSide.dto.OrderManagement;

import com.jkgroup.foodCourtServerSide.enums.OrderStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderManagementDTO { // To Management
    private long orderId;
    private String address;
    private String phone;
    private Double totalPayment;
    private String paymentMethod;
    private String deliveryTime;
    private String notes;
    private OrderStatusEnum orderStatusEnum;

    // List Of Order Item Management DTO
    private List<OrderItemManagementDTO> orderItems;

    // Fields from BaseEntity
    private LocalDateTime createdAt;
    private String createdBy;
    private String updatedBy;
    private boolean isDeleted;
}
