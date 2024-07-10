package com.jkgroup.foodCourtServerSide.dto.Order;

import com.jkgroup.foodCourtServerSide.dto.OrderItem.OrderItemDTO;
import com.jkgroup.foodCourtServerSide.enums.CategoryEnum;
import com.jkgroup.foodCourtServerSide.enums.OrderStatusEnum;
import com.jkgroup.foodCourtServerSide.model.OrderItem;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private int userId;
    private String address;
    private String phone;
    private Double totalPayment;
    private String paymentMethod;
    private String deliveryTime;
    private String notes;


    private OrderStatusEnum orderStatusEnum;

    @NotNull(message = "Order item list cannot be null")
    @NotEmpty(message = "Order item list cannot be empty")
    private List<OrderItemDTO> orderItems;

    // Fields from BaseEntity
    private LocalDateTime createdAt;
    private String createdBy;
    private String updatedBy;
    private boolean isDeleted;
}
