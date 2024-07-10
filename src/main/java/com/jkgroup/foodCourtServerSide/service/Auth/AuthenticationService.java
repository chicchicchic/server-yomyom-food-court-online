package com.jkgroup.foodCourtServerSide.service.Auth;

import com.jkgroup.foodCourtServerSide.model.Auth.AuthenticationRequest;
import com.jkgroup.foodCourtServerSide.model.Auth.AuthenticationResponse;

import java.io.IOException;

public interface AuthenticationService {
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws IOException;
}
