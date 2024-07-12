package com.jkgroup.foodCourtServerSide.controller.Auth;


import com.jkgroup.foodCourtServerSide.constant.Exception.ExceptionConstant;
import com.jkgroup.foodCourtServerSide.exception.ValidationException;
import com.jkgroup.foodCourtServerSide.model.Auth.AuthenticationRequest;
import com.jkgroup.foodCourtServerSide.model.Auth.RegisterRequest;
import com.jkgroup.foodCourtServerSide.service.Auth.AuthenticationServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Tag(name = "Auth REST Controller", description = "Endpoints related to Auth")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;

    // [POST] Register New User
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            throw new ValidationException(ExceptionConstant.VALIDATION_ERROR, errors);
        }

        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);
    }

    // [POST] Login
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody AuthenticationRequest request,
            BindingResult bindingResult
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            throw new ValidationException(ExceptionConstant.VALIDATION_ERROR, errors);
        }

        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);

    }

    // [POST] Refresh Token When Access Token Expired
    @RequestMapping(value = "/refresh-token", method = RequestMethod.POST)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }

}
