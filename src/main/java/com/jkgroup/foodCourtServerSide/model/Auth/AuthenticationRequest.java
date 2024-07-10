package com.jkgroup.foodCourtServerSide.model.Auth;

import com.jkgroup.foodCourtServerSide.constant.User.UserConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class AuthenticationRequest {
    @Pattern(regexp = UserConstant.EMAIL_REGEX, message = "Invalid email format. You need ...@....")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
