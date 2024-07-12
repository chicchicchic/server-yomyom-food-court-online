package com.jkgroup.foodCourtServerSide.service.OrderItem;

import org.springframework.http.ResponseEntity;

public interface OrderItemService {
    // [PUT]
    public ResponseEntity<?> updateOrderItemStatus(Long orderItemId, String newStatus) throws Exception;
}
