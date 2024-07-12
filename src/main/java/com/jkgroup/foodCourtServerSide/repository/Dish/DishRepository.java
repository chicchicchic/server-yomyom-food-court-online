package com.jkgroup.foodCourtServerSide.repository.Dish;

import com.jkgroup.foodCourtServerSide.enums.CategoryEnum;
import com.jkgroup.foodCourtServerSide.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {
    // If you're using Spring Data JPA repositories, methods like findByIsDeletedFalse() provide you with a shorthand way of defining queries based on method names. Behind the scenes, Spring Data JPA translates these method names into SQL queries that filter records based on the field names and criteria specified in the method name. For example, findByIsDeletedFalse() is translated into a SQL query similar to: "SELECT * FROM tbl_dish WHERE is_deleted = false;"
    Page<Dish> findByIsDeletedFalse(Pageable pageable);

    // Get all items by category enum and no soft-delete
    Page<Dish> findByCategoryEnumAndIsDeletedFalse(CategoryEnum categoryName, Pageable pageable);

    // Trash Dish List
    Page<Dish> findByIsDeletedTrue(Pageable pageable);

    // Find All By DishId In
    Page<Dish> findAllByDishIdIn(Set<Integer> dishIds, Pageable pageable);

    @Query(name = "findAllByName", value = "select d from Dish d where d.name like %?1% and d.isDeleted = false ")
    public Page<Dish> findAllByName(String name, Pageable pageable);

    @Query(name = "findAllIsEqualAndLessThanThePrice", value = "select d from Dish d where (case when d.discount > 0 then d.originalPrice * (1 - d.discount / 100) else d.originalPrice end) <= :price and d.isDeleted = false")
    public Page<Dish> findAllIsEqualAndLessThanThePrice(BigDecimal price, Pageable pageable);


}
