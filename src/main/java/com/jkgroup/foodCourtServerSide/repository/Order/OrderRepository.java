package com.jkgroup.foodCourtServerSide.repository.Order;

import com.jkgroup.foodCourtServerSide.enums.OrderStatusEnum;
import com.jkgroup.foodCourtServerSide.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderStatusEnumAndUserId(OrderStatusEnum orderStatusEnum, int userId);
    List<Order> findByUserId(int userId);
    Page<Order> findByIsDeletedFalse(Pageable pageable);
    Page<Order> findByIsDeletedTrue(Pageable pageable);
}
