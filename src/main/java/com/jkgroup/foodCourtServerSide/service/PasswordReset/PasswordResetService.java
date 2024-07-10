package com.jkgroup.foodCourtServerSide.service.PasswordReset;

import com.jkgroup.foodCourtServerSide.model.User;
import com.jkgroup.foodCourtServerSide.repository.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String sendResetToken(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            userRepository.save(user);

            // Send email with token (implement email sending logic)
            return token; // Return the token
        }
        throw new IllegalArgumentException("Email not found");
    }

    public void resetPassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null); // Remove reset password token after reset successfully
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }
}
