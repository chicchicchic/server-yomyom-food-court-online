package com.jkgroup.foodCourtServerSide.repository.User;

import com.jkgroup.foodCourtServerSide.model.Dish;
import com.jkgroup.foodCourtServerSide.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // [SPECIAL VALIDATION] To validate ADDING ITEM for DUPLICATE cases (check all values in the Database)
    boolean existsByUserName(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    // [SPECIAL VALIDATION] To validate UPDATING ITEM for DUPLICATE cases (check all values in the Database EXCEPT current value)
    boolean existsByPhoneAndUserIdNot(String phone, int id);
    boolean existsByEmailAndUserIdNot(String phone, int id);


    // If you're using Spring Data JPA repositories, methods like findByIsDeletedFalse() provide you with a shorthand way of defining queries based on method names. Behind the scenes, Spring Data JPA translates these method names into SQL queries that filter records based on the field names and criteria specified in the method name. For example, findByIsDeletedFalse() is translated into a SQL query similar to: "SELECT * FROM tbl_dish WHERE is_deleted = false;"
    Page<User> findByIsDeletedFalse(Pageable pageable);

    // Trash Dish User
    Page<User> findByIsDeletedTrue(Pageable pageable);

    // Find all by First Name or Last Name
    @Query("SELECT u FROM User u WHERE u.isDeleted = false AND (u.firstName LIKE %:searchText% OR u.lastName LIKE %:searchText%)")
    Page<User> findAllByName(@Param("searchText") String searchText, Pageable pageable);

    // Find all by Phone Number
    @Query("SELECT u FROM User u WHERE u.isDeleted = false AND u.phone LIKE %:searchText%")
    Page<User> findAllByPhone(@Param("searchText") String searchText, Pageable pageable);

    // Find one by Email
    Optional<User> findByEmail(String email);

    // Find user by reset password token
    Optional<User> findByResetToken(String resetToken);
}
