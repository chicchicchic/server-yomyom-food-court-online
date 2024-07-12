package com.jkgroup.foodCourtServerSide.service.OrderItem;

import com.jkgroup.foodCourtServerSide.enums.OrderItemStatusEnum;
import com.jkgroup.foodCourtServerSide.model.OrderItem;
import com.jkgroup.foodCourtServerSide.repository.OrderItem.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService{
    @Autowired
    private OrderItemRepository orderItemRepository;


    // [PUT]
    @Override
    public ResponseEntity<?> updateOrderItemStatus(Long orderItemId, String newStatus) throws Exception {
        Optional<OrderItem> orderItemFound = orderItemRepository.findById(orderItemId);

        if (orderItemFound.isPresent()) {
            OrderItem orderItem = orderItemFound.get();
            orderItem.setOrderItemStatusEnum(OrderItemStatusEnum.valueOf(newStatus));

            orderItemRepository.save(orderItem);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Status Of The Order Item Changed Successfully!");
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No items found!");
        }
    }

}
