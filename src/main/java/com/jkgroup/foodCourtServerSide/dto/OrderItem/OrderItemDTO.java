package com.jkgroup.foodCourtServerSide.dto.OrderItem;

import com.jkgroup.foodCourtServerSide.enums.OrderItemStatusEnum;
import com.jkgroup.foodCourtServerSide.enums.OrderStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderItemDTO {
    private int dishId;
    private int quantity;
    private Double totalPrice;

    private OrderItemStatusEnum orderItemStatusEnum;


    // Fields from BaseEntity
    private LocalDateTime createdAt;
    private String createdBy;
    private String updatedBy;
    private boolean isDeleted;
}
