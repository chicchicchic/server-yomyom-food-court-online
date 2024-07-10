package com.jkgroup.foodCourtServerSide.model.Auth;


import com.jkgroup.foodCourtServerSide.constant.User.UserConstant;
import com.jkgroup.foodCourtServerSide.enums.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 10, message = "Username must be between 3 and 10 characters")
    private String userName;

    @NotBlank(message = "First Name cannot be empty")
    private String firstName;

    @NotBlank(message = "last Name cannot be empty")
    private String lastName;

    @NotBlank(message = "Email cannot be empty")
    @Pattern(regexp = UserConstant.EMAIL_REGEX, message = "Invalid email format. You need ...@....")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 15, message = "Password length must be between 8 and 15 characters")
    private String password;

    @NotBlank(message = "Phone cannot be empty")
    @Pattern(regexp = UserConstant.PHONE_REGEX, message = "Invalid phone format. You need 10-11 digits")
    private String phone;

//    @Pattern(
//            regexp = UserConstant.DATE_OF_BIRTH_REGEX,
//            message = "Date of birth must be in the format YYYY-MM-DD and between 1920 and the current year"
//    )
    @NotNull(message = "Date of birth cannot be null")
    private LocalDateTime dateOfBirth;

    private UserRoleEnum userRoleEnum;


    // Fields from BaseEntity
    private LocalDateTime createdAt;
    private String createdBy;
    private String updatedBy;
    private boolean isDeleted;
}
