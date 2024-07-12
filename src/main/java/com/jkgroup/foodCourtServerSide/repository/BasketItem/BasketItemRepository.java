package com.jkgroup.foodCourtServerSide.repository.BasketItem;

import com.jkgroup.foodCourtServerSide.model.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, Integer> {

}
