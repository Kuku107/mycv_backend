package com.viettel.mycv.service;

import com.viettel.mycv.dto.request.SignInRequest;
import com.viettel.mycv.dto.response.TokenResponse;
import org.antlr.v4.runtime.Token;

import java.util.Map;

public interface AuthenticationService {
    TokenResponse getToken(SignInRequest request);

    TokenResponse refreshToken(String refreshToken);
}
