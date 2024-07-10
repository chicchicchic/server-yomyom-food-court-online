package com.jkgroup.foodCourtServerSide.service.Basket;


import com.jkgroup.foodCourtServerSide.dto.Basket.BasketDTO;
import com.jkgroup.foodCourtServerSide.dto.BasketItem.BasketItemDTO;
import com.jkgroup.foodCourtServerSide.model.Basket;
import com.jkgroup.foodCourtServerSide.model.BasketItem;
import com.jkgroup.foodCourtServerSide.model.Dish;
import com.jkgroup.foodCourtServerSide.repository.Basket.BasketRepository;
import com.jkgroup.foodCourtServerSide.repository.Dish.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BasketServiceImpl implements BasketService {
    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private DishRepository dishRepository;

    // Add Basket Item To Basket
    @Override
    public Basket addItemToBasket(BasketDTO basketDTO, BasketItem basketItem) {
        Optional<Basket> optionalBasket = basketRepository.findByUserId(basketDTO.getUserId());
        Basket basket;
        if (optionalBasket.isPresent()) {
            basket = optionalBasket.get();
        } else {
            basket = new Basket();
            basket.setUserId(basketDTO.getUserId());
            basket.setCreatedAt(LocalDateTime.now());
            basket.setCreatedBy(basketDTO.getCreatedBy());
            basket.setUpdatedBy(basketDTO.getUpdatedBy());
            basket.setDeleted(false);
            basket.setBasketItemList(new ArrayList<>()); // Initialize the basketItemList if new basket
        }

        boolean itemExists = false;
        for (BasketItem item : basket.getBasketItemList()) {
            if (item.getDishId() == basketItem.getDishId()) {
                item.setQuantity(item.getQuantity() + basketItem.getQuantity());
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {

            basketItem.setCreatedAt(LocalDateTime.now());
            basketItem.setCreatedBy(basketItem.getCreatedBy());
            basketItem.setUpdatedBy(basketItem.getUpdatedBy());
            basketItem.setDeleted(false);

            basket.getBasketItemList().add(basketItem);
        }

        return basketRepository.save(basket);
    }


    // Get Basket Items By UserId
    @Override
    public List<BasketItemDTO> getBasketItemsByUserId(int userId) {
        Optional<Basket> basketOptional = basketRepository.findByUserId(userId);
        if (basketOptional.isPresent()) {
            Basket basket = basketOptional.get();
            return basket.getBasketItemList().stream()
                    .map(basketItem -> {
                        Dish dish = dishRepository.findById(basketItem.getDishId()).orElse(null);
                        if (dish != null) {
                            return new BasketItemDTO(dish, basketItem.getQuantity());
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList(); // Basket not found for the given user
        }
    }

    // Get Basket By UserId
    @Override
    public Optional<Basket> getBasketByUserId(int userId) {
        Optional<Basket> basketOptional = basketRepository.findByUserId(userId);
        return basketOptional;
    }

    // Count All Items In The Basket
    @Override
    public int countBasketItemsByUserId(int userId) {
        Optional<Basket> basketOptional = basketRepository.findByUserId(userId);

        // [Count] (NOT include quantity: 2 dish A, 3 dish B => total: 2)
        if (basketOptional.isPresent()) {
            Basket basket = basketOptional.get();
            Set<Integer> distinctDishIds = new HashSet<>();

            // Iterate through basket items to collect distinct dish IDs
            for (BasketItem basketItem : basket.getBasketItemList()) {
                distinctDishIds.add(basketItem.getDishId());
            }

            // Return the count of distinct dish IDs
            return distinctDishIds.size();
        } else {
            return 0;
        }


        // [Count] (include quantity: 2 dish A, 3 dish B => total: 5)
//        if (basketOptional.isPresent()) {
//            Basket basket = basketOptional.get();
//            return basket.getBasketItemList().stream().mapToInt(BasketItem::getQuantity).sum();
//        } else {
//            return 0;
//        }
    }

    // Remove Item From Basket
    @Override
    public void removeItemFromBasket(int userId, int dishId) {
        Optional<Basket> optionalBasket = basketRepository.findByUserId(userId);
        if (optionalBasket.isPresent()) {
            Basket basket = optionalBasket.get();
            basket.getBasketItemList().removeIf(item -> item.getDishId() == dishId);
            basketRepository.save(basket);
        }
    }

    // Remove All Item From Basket By UserId
    @Override
    public void removeAllBasketItemsByUserId(int userId) {
        Optional<Basket> optionalBasket = basketRepository.findByUserId(userId);

        if (optionalBasket.isPresent()) {
            Basket basket = optionalBasket.get();
            basket.getBasketItemList().clear();
            basketRepository.save(basket);
        }
    }
}