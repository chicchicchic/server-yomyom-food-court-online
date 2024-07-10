package com.jkgroup.foodCourtServerSide.model;

import com.jkgroup.foodCourtServerSide.enums.CategoryEnum;
import com.jkgroup.foodCourtServerSide.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "tbl_order")
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long orderId;
    private int userId;
    private String address;
    private String phone;
    private Double totalPayment;
    private String paymentMethod;
    private String deliveryTime;
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
//    @JsonIgnore
    private OrderStatusEnum orderStatusEnum;


    // [ONE - MANY] Order - Order Item
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
}
