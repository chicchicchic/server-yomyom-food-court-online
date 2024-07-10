package com.jkgroup.foodCourtServerSide.controller.Order;

import com.jkgroup.foodCourtServerSide.constant.Exception.ExceptionConstant;
import com.jkgroup.foodCourtServerSide.dto.Order.OrderDTO;
import com.jkgroup.foodCourtServerSide.enums.CategoryEnum;
import com.jkgroup.foodCourtServerSide.enums.OrderItemStatusEnum;
import com.jkgroup.foodCourtServerSide.enums.OrderStatusEnum;
import com.jkgroup.foodCourtServerSide.exception.ValidationException;
import com.jkgroup.foodCourtServerSide.model.Dish;
import com.jkgroup.foodCourtServerSide.model.Order;
import com.jkgroup.foodCourtServerSide.model.OrderItem;
import com.jkgroup.foodCourtServerSide.service.Order.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Tag(name = "Order REST Controller", description = "Endpoints related to Order")
@RequestMapping("/api/order")
public class OrderRestController {
    @Autowired
    private OrderService orderService;


    // [GET] Order Item Status Enum List
    @RequestMapping(value = "/order-item-status-list", method = RequestMethod.GET)
    public ResponseEntity<?> orderItemStatusEnumList() {
        List<OrderItemStatusEnum> orderItemStatusEnums = Arrays.asList(OrderItemStatusEnum.values());

        // Throw exception if the list is empty (case 1), is inaccessible (case 2)
        if (orderItemStatusEnums == null) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve Order Item Statuses. Please try again later!");
        } else if (orderItemStatusEnums.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Order Item Statuses found!");
        } else {
            return ResponseEntity
                    .ok(orderItemStatusEnums);
        }
    }

    // [GET] Get All Order Items In The Order By UserId
    @GetMapping("/get-order-items-by-user-id")
    public List<OrderItem> getOrdersByStatusAndUserId(
            @RequestParam("status") OrderStatusEnum status,
            @RequestParam("userId") int userId) {
        return orderService.getOrderItemsByStatusAndUserId(status, userId);
    }

    // [GET] Get Dishes By UserId and Status
    @GetMapping("/detail-order-tracking")
    public ResponseEntity<?> getDishesByUserIdAndStatus(
            @RequestParam("status") OrderItemStatusEnum status,
            @RequestParam("userId") int userId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "2") int pageSize
    ) {
        Page<Dish> dishesList = orderService.getDishesByUserIdAndStatus(userId, status, pageNumber, pageSize);

        if (dishesList == null) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve dishes. Please try again later!");
        } else if (dishesList.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No dishes found!");
        } else {
            return ResponseEntity
                    .ok(dishesList);
        }
    }

    // [POST] Create Order
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) { // Check for validation errors
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
//            System.out.println("ERRORS :" + errors.toString());
            throw new ValidationException(ExceptionConstant.VALIDATION_ERROR, errors);
        }

        Order createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrder);
    }

}
