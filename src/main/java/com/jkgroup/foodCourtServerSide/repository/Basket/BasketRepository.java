package com.jkgroup.foodCourtServerSide.repository.Basket;

import com.jkgroup.foodCourtServerSide.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Integer> {
    Optional<Basket> findByUserId(int userId);
}
