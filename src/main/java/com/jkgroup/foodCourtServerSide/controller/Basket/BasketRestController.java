package com.jkgroup.foodCourtServerSide.controller.Basket;

import com.jkgroup.foodCourtServerSide.dto.Basket.BasketDTO;
import com.jkgroup.foodCourtServerSide.dto.Basket.UpdateQuantityDTO;
import com.jkgroup.foodCourtServerSide.dto.BasketItem.BasketItemDTO;
import com.jkgroup.foodCourtServerSide.model.Basket;
import com.jkgroup.foodCourtServerSide.model.BasketItem;
import com.jkgroup.foodCourtServerSide.repository.Basket.BasketRepository;
import com.jkgroup.foodCourtServerSide.service.Basket.BasketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Basket REST Controller", description = "Endpoints related to Basket")
@RequestMapping("/api/basket")
public class BasketRestController {
    @Autowired
    private BasketService basketService;

    @Autowired
    private BasketRepository basketRepository;


    // [GET] Get Basket Items By UserId
    @GetMapping("/basket-item-list/{userId}")
    public ResponseEntity<?> getBasketItemsByUserId(@PathVariable int userId) {
        List<BasketItemDTO> basketItemListByUserId = basketService.getBasketItemsByUserId(userId);

        if (basketItemListByUserId == null) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve basket item list. Please try again later!");
        } else if (basketItemListByUserId.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No basket item list found!");
        } else {
            return ResponseEntity
                    .ok(basketItemListByUserId);
        }
    }

    // [GET] Count All Basket Items In The Basket
    @GetMapping("/count-all-items/{userId}")
    public int countBasketItems(@PathVariable int userId) {
        return basketService.countBasketItemsByUserId(userId);
    }

    // [POST] Add Item To Basket From Home Page
    @PostMapping("/add-item")
    public Basket addItemToCart(@Valid @RequestBody BasketDTO basketDTO) {
        return basketService.addItemToBasket(basketDTO, basketDTO.getBasketItemList().get(0));
    }

    // [PUT] Update Item Quantity
    @PutMapping("/update-item-quantity/{dishId}")
    public ResponseEntity<?> updateItemQuantity(
            @PathVariable int dishId,
            @RequestBody UpdateQuantityDTO updateQuantityDTO) {
        Optional<Basket> optionalBasket = basketService.getBasketByUserId(updateQuantityDTO.getUserId());  // Get user ID from token
        if (!optionalBasket.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Basket basket = optionalBasket.get();
        boolean itemFound = false;
        for (BasketItem item : basket.getBasketItemList()) {
            if (item.getDishId() == dishId) {
                item.setQuantity(updateQuantityDTO.getQuantity());
                itemFound = true;
                break;
            }
        }

        if (!itemFound) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No item found!");  // Item not found in basket
        }

        basketRepository.save(basket);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("The quantity updated successfully!");
    }

    // [DELETE] Delete Item From Basket
    @DeleteMapping("/remove-item/{dishId}")
    public ResponseEntity<?> removeItemFromBasket(
            @PathVariable int dishId,
            @RequestParam int userId
    ) {
        Optional<Basket> optionalBasket = basketService.getBasketByUserId(userId);
        if (!optionalBasket.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Basket not found!");
        }

        Basket basket = optionalBasket.get();
        boolean itemRemoved = basket.getBasketItemList().removeIf(item -> item.getDishId() == dishId);

        if (!itemRemoved) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found in basket!");
        }

        basketRepository.save(basket);
        return ResponseEntity.status(HttpStatus.OK).body("Item removed successfully!");
    }

    // [DELETE] Clear All Items From Basket By UserId
    @DeleteMapping("/clear-all-items-from-basket/{userId}")
    public ResponseEntity<?> removeAllBasketItemsByUserId(@PathVariable int userId) {
        basketService.removeAllBasketItemsByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("All items removed from the basket for userId: " + userId);
    }
}
