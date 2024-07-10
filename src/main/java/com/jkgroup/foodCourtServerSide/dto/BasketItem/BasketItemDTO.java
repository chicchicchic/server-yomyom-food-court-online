package com.jkgroup.foodCourtServerSide.dto.BasketItem;

import com.jkgroup.foodCourtServerSide.model.Dish;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BasketItemDTO {
    private Dish dish;
    private int quantity;

    public BasketItemDTO(Dish dish, int quantity) {
        this.dish = dish;
        this.quantity = quantity;
    }
}
