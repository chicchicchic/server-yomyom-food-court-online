package com.jkgroup.foodCourtServerSide.controller.Order;

import com.jkgroup.foodCourtServerSide.dto.OrderManagement.OrderManagementDTO;
import com.jkgroup.foodCourtServerSide.enums.OrderItemStatusEnum;
import com.jkgroup.foodCourtServerSide.enums.OrderStatusEnum;
import com.jkgroup.foodCourtServerSide.service.Dish.DishServiceImpl;
import com.jkgroup.foodCourtServerSide.service.Order.OrderService;
import com.jkgroup.foodCourtServerSide.service.OrderItem.OrderItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@Tag(name = "Order Management REST Controller", description = "Endpoints related to Order Management")
@RequestMapping("/api/order-management")
public class OrderManagementRestController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private DishServiceImpl dishService;


    // [GET] List Order Item Status Enums
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
                    .body("No Order Item Statuses Found!");
        } else {
            return ResponseEntity
                    .ok(orderItemStatusEnums);
        }
    }

    // [GET] All Orders And Their Order Items (isDeleted: false)
    @GetMapping("/order-list")
    public ResponseEntity<?> getAllOrders(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "2") int pageSize)
    {
        try {
            Page<OrderManagementDTO> itemsList = orderService.getAllOrders(pageNumber, pageSize);

            if (itemsList.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No orders found!");
            } else {
                return ResponseEntity
                        .ok(itemsList);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve orders. Please try again later!");
        }
    }

    // [GET] All Orders Trash And Their Order Items (isDeleted: true)
    @GetMapping("/order-trash-list")
    public ResponseEntity<?> getAllOrdersTrash(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "2") int pageSize)
    {
        try {
            Page<OrderManagementDTO> itemsList = orderService.getAllOrdersTrash(pageNumber, pageSize);

            if (itemsList.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No orders found!");
            } else {
                return ResponseEntity
                        .ok(itemsList);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve orders. Please try again later!");
        }
    }

    // [PUT] Update Order Item Status
    @PutMapping("/update-order-item-status/{orderItemId}")
    public ResponseEntity<String> updateOrderItemStatus(
            @PathVariable Long orderItemId,
            @RequestParam("newStatus") String newStatus)
    {
        try {
            orderItemService.updateOrderItemStatus(orderItemId, newStatus);
            return ResponseEntity
                    .ok("Order item status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating order item status.");
        }
    }

    // [PUT] Update Order Status
    @PutMapping("/update-order-status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam("newStatus") OrderStatusEnum newStatus,
            @RequestParam("isDeleted") boolean isDeleted)
    {
        try {
            orderService.updateOrderStatus(orderId, newStatus, isDeleted);
            return ResponseEntity
                    .ok("Order status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating order status.");
        }
    }

}
