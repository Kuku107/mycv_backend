package com.viettel.mycv.controller;

import com.viettel.mycv.dto.request.SignInRequest;
import com.viettel.mycv.dto.response.TokenResponse;
import com.viettel.mycv.service.AuthenticationService;
import com.viettel.mycv.service.impl.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@Slf4j(topic="AUTHENTICATION-CONTROLLER")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;
    private final CookieService cookieService;

    @PostMapping("/get-token")
    public TokenResponse getToken(HttpServletResponse response, @RequestBody SignInRequest request) {
        log.info("Get request get access token and refresh token");

        TokenResponse data = authService.getToken(request);
        cookieService.addAccessTokenToCookie(response, data.getAccessToken());
        cookieService.addRefreshTokenToCookie(response, data.getRefreshToken());

        log.info("Finish request get access token and refresh token");
        return data;
    }

    @PostMapping("/refresh-token")
    public TokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get request make new access token");
        String refreshToken = cookieService.getTokenByName(request, "refresh-token");

        TokenResponse data = authService.refreshToken(refreshToken);
        cookieService.addAccessTokenToCookie(response, data.getAccessToken());
        log.info("Finish request refresh token");
        return data;
    }
}
