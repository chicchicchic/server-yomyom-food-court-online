package com.jkgroup.foodCourtServerSide.service.Auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jkgroup.foodCourtServerSide.constant.Exception.ExceptionConstant;
import com.jkgroup.foodCourtServerSide.enums.UserRoleEnum;

import com.jkgroup.foodCourtServerSide.exception.ValidationException;
import com.jkgroup.foodCourtServerSide.model.User;
import com.jkgroup.foodCourtServerSide.model.Auth.AuthenticationRequest;
import com.jkgroup.foodCourtServerSide.model.Auth.AuthenticationResponse;
import com.jkgroup.foodCourtServerSide.model.Auth.RegisterRequest;

import com.jkgroup.foodCourtServerSide.repository.User.UserRepository;
import com.jkgroup.foodCourtServerSide.service.JWT.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository repository;
//    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    // Register New User
    public AuthenticationResponse register(RegisterRequest request) {
//        var user = User.builder()
//                .userName(request.getUsername())
//                .firstName(request.getFirstname())
//                .lastName(request.getLastname())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .phone(request.getPhone())
//                .dateOfBirth(request.getDateOfBirth())
//                .roleEnum(UserRoleEnum.CUSTOMER)
//                .build();
        List<String> errors = new ArrayList<>();

        // Check if the username already exists
        if (userRepository.existsByUserName(request.getUserName())) {
            errors.add("Username already exists");
        }

        // Check if the email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            errors.add("Email already exists");
        }

        // Check if the phone already exists
        if (userRepository.existsByPhone(request.getPhone())) {
            errors.add("Phone already exists");
        }

        // If there are validation errors, throw ValidationException
        if (!errors.isEmpty()) {
            throw new ValidationException(ExceptionConstant.DUPLICATE_ERROR, errors);
        }

        User user = new User();
        user.setUserName(request.getUserName());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setRoleEnum(UserRoleEnum.CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy("System");
        user.setUpdatedBy("System");
        user.setDeleted(false);

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
//        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Login
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws IOException{
        List<String> errors = new ArrayList<>();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            errors.add("Invalid email or password");
            // You can add more error into the error list
            throw new ValidationException(ExceptionConstant.INVALID_INFOMATION, errors);
        }

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    errors.add("The user not found");
                    return new ValidationException(ExceptionConstant.NOT_FOUND, errors);
                });

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
//        revokeAllUserTokens(user);
//        saveUserToken(user, jwtToken);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    // Refresh Access Token
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                var authResponse = AuthenticationResponse.builder()
                        .token(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }


//    private void saveUserToken(User user, String jwtToken) {
//        var token = Token.builder()
//                .user(user)
//                .token(jwtToken)
//                .tokenType(TokenType.BEARER)
//                .expired(false)
//                .revoked(false)
//                .build();
//        tokenRepository.save(token);
//    }
//
//    private void revokeAllUserTokens(User user) {
//        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
//        if (validUserTokens.isEmpty())
//            return;
//        validUserTokens.forEach(token -> {
//            token.setExpired(true);
//            token.setRevoked(true);
//        });
//        tokenRepository.saveAll(validUserTokens);
//    }
//
//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        final String refreshToken;
//        final String userEmail;
//        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        userEmail = jwtService.extractUsername(refreshToken);
//        if (userEmail != null) {
//            var user = this.repository.findByEmail(userEmail)
//                    .orElseThrow();
//            if (jwtService.isTokenValid(refreshToken, user)) {
//                var accessToken = jwtService.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                var authResponse = AuthenticationResponse.builder()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
//    }
}
