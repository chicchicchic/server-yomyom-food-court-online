package com.jkgroup.foodCourtServerSide.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jkgroup.foodCourtServerSide.enums.OrderItemStatusEnum;
import com.jkgroup.foodCourtServerSide.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "tbl_order_item")
public class OrderItem extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private long orderItemId;
    private int dishId;
    private int quantity;
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
//    @JsonIgnore
    private OrderItemStatusEnum orderItemStatusEnum;

    // [ONE - MANY] Order Item - Order
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
}
