package com.jkgroup.foodCourtServerSide.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_basket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Basket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int basketId;
    private int userId;

    // [ONE - MANY] Basket - Basket Item
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "basket_id")
    private List<BasketItem> basketItemList = new ArrayList<>(); // Initialize with an empty list
}
