package com.jkgroup.foodCourtServerSide.controller.PasswordReset;

import com.jkgroup.foodCourtServerSide.dto.ResetPassword.PasswordResetDTO;
import com.jkgroup.foodCourtServerSide.dto.ResetPassword.PasswordResetRequest;
import com.jkgroup.foodCourtServerSide.service.PasswordReset.PasswordResetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@Tag(name = "Reset Password REST Controller", description = "Endpoints related to Reset Password")
@RequestMapping("/api/reset-password")
@RequiredArgsConstructor
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    // [POST] Send Reset Password Token Via Email
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetRequest request) {
        String token = passwordResetService.sendResetToken(request.getEmail());
        return ResponseEntity.ok(new HashMap() {{
            put("message", "Reset token sent to email");
            put("token", token);  // Include token in the response
        }});
    }

    // [POST] Reset Token
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDTO resetDTO) {
        passwordResetService.resetPassword(resetDTO.getToken(), resetDTO.getNewPassword());
        return ResponseEntity.ok("Password reset successfully");
    }
}
