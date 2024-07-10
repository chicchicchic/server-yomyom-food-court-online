package com.jkgroup.foodCourtServerSide.dto.ResetPassword;

import lombok.Data;

@Data
public class PasswordResetDTO {
    private String token;
    private String newPassword;
}
