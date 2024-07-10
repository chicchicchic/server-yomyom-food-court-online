package com.jkgroup.foodCourtServerSide.dto.OrderManagement;

import com.jkgroup.foodCourtServerSide.enums.OrderItemStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderItemManagementDTO { // To Management
    private long orderItemId;
    private int quantity;
    private Double totalPrice;
    private OrderItemStatusEnum orderItemStatusEnum;

    // Fields from Dish Entity
    private String dishName;
    private String dishImage;

    // Fields from BaseEntity
    private LocalDateTime createdAt;
    private String createdBy;
    private String updatedBy;
    private boolean isDeleted;
}
