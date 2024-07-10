package com.jkgroup.foodCourtServerSide.dto.Dish;


import com.jkgroup.foodCourtServerSide.enums.CategoryEnum;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DishDTO {
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 1, max = 100, message = "Name length must be between 1 and 100 characters")
    private String name;

    //    @Positive(message = "Original price must be positive (> $0)") // only greater than 0
    //    @Max(value = 5000, message = "Original price must be less than or equal to $5000")
    @NotNull(message = "Original price cannot be null")
    @DecimalMin(value = "1.00", message = "Original price must be greater than or equal $1")
    @DecimalMax(value = "5000.00", message = "Original price must be less than or equal to $5000")
    private BigDecimal originalPrice;

    //    @PositiveOrZero(message = "Discount must be positive or zero (>= 0%)") // greater than and equal 0
    //    @Max(value = 100, message = "Discount must be less than or equal to 100%")
    @DecimalMin(value = "0.0", message = "Discount must be greater than or equal to 0%")
    @DecimalMax(value = "100.0", message = "Discount must be less than or equal to 100%")
    private double discount;

    @NotNull(message = "Preparation time cannot be null")
    @Min(value = 20, message = "Preparation time must be greater than or equal to 20 minutes")
    @Max(value = 200, message = "Preparation time must be less than or equal to 200 minutes")
    private int preparationTime;

    @NotBlank(message = "Meal set cannot be empty")
    @Size(min = 1, max = 1000, message = "Meal set length must be between 1 and 1000 characters")
    private String mealSet;

    @NotNull(message = "Image cannot be null")
    private MultipartFile image;

    @NotNull(message = "Category cannot be null")
    private CategoryEnum categoryEnum;

    // Fields from BaseEntity
    private LocalDateTime createdAt;
    private String createdBy;
    private String updatedBy;
    private boolean isDeleted;
}
