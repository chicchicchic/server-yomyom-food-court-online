package com.jkgroup.foodCourtServerSide.dto.Basket;

import com.jkgroup.foodCourtServerSide.model.BasketItem;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BasketDTO {
    private int userId;

    @NotNull(message = "Basket item list cannot be null")
    @NotEmpty(message = "Basket item list cannot be empty")
    private List<BasketItem> basketItemList = new ArrayList<>();

    // Fields from BaseEntity
    private LocalDateTime createdAt;
    private String createdBy;
    private String updatedBy;
    private boolean isDeleted;
}
