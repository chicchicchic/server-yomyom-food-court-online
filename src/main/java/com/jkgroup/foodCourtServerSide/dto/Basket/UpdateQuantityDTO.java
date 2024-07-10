package com.jkgroup.foodCourtServerSide.dto.Basket;

import lombok.Data;

@Data
public class UpdateQuantityDTO {
    private int userId;
    private int quantity;
}
