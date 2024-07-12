package com.jkgroup.foodCourtServerSide.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jkgroup.foodCourtServerSide.enums.CategoryEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "tbl_dish")
public class Dish extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dish_id")
    private int dishId;
    private String name;

    @Column(nullable = true)
    private BigDecimal originalPrice; // (unit: $), calculating the discounted price dynamically when needed base on "originalPrice" and "discount"

    @Column(nullable = true)
    private double discount; // (unit: %)
    // Calculating the discounted price dynamically when needed

    @Column(nullable = true)
    private int preparationTime; // Preparation time is the total time from preparation to completion of the dish (unit: minutes)

    @Column(nullable = true)
    private String mealSet; // Meal set is items in package that customer will receive

//    @JsonIgnore
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;


    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
//    @JsonIgnore
    private CategoryEnum categoryEnum;

}
