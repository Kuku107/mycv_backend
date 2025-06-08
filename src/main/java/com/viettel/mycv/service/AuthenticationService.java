package com.viettel.mycv.service;

import com.viettel.mycv.dto.request.SignInRequest;
import com.viettel.mycv.dto.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse getToken(SignInRequest request);

    TokenResponse refreshToken(String refreshToken);

    Long getContextUserId();
}
