package com.jkgroup.foodCourtServerSide.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_basket_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BasketItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_item_id")
    private int basketItemId;
    private int dishId;
    private int quantity;
}
