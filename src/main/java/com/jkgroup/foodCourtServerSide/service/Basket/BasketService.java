package com.jkgroup.foodCourtServerSide.service.Basket;

import com.jkgroup.foodCourtServerSide.dto.Basket.BasketDTO;
import com.jkgroup.foodCourtServerSide.dto.BasketItem.BasketItemDTO;
import com.jkgroup.foodCourtServerSide.model.Basket;
import com.jkgroup.foodCourtServerSide.model.BasketItem;

import java.util.List;
import java.util.Optional;

public interface BasketService {
    public Basket addItemToBasket(BasketDTO basketDTO, BasketItem basketItem);

    public List<BasketItemDTO> getBasketItemsByUserId(int userId);

    public Optional<Basket> getBasketByUserId(int userId);

    public int countBasketItemsByUserId(int userId);

    public void removeItemFromBasket(int userId, int dishId);

    public void removeAllBasketItemsByUserId(int userId);

}
