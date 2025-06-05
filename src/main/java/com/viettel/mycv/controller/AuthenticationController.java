package com.viettel.mycv.controller;

import com.viettel.mycv.exception.UnauthenticationException;
import com.viettel.mycv.common.UserStatus;
import com.viettel.mycv.dto.request.SignInRequest;
import com.viettel.mycv.dto.response.TokenResponse;
import com.viettel.mycv.exception.UserNotVerifiedException;
import com.viettel.mycv.model.UserEntity;
import com.viettel.mycv.service.AuthenticationService;
import com.viettel.mycv.service.EmailService;
import com.viettel.mycv.service.MyUserDetailsService;
import com.viettel.mycv.service.UserService;
import com.viettel.mycv.service.impl.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;


@RestController
@RequestMapping("/auth")
@Slf4j(topic="AUTHENTICATION-CONTROLLER")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;
    private final CookieService cookieService;
    private final MyUserDetailsService myUserDetailsService;
    private final EmailService emailService;

    @PostMapping("/get-token")
    public TokenResponse getToken(HttpServletResponse response, @RequestBody SignInRequest request) {
        log.info("Get request get access token and refresh token");

        UserEntity user = myUserDetailsService.loadUserByUsername(request.getEmail());

        if (user.getUserStatus() == UserStatus.NONE) {
            emailService.verificationEmail(user.getEmail(), user.getUsername(), user.getId());
            throw new UserNotVerifiedException("User is not verified, please verify your email.");
        }

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
        try {
            TokenResponse data = authService.refreshToken(refreshToken);
            cookieService.addAccessTokenToCookie(response, data.getAccessToken());
            log.info("Finish request refresh token");
            return data;
        } catch (IllegalArgumentException e) {
            throw new UnauthenticationException("Please login first");
        }

    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get request logout");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                Cookie deleted = new Cookie(cookie.getName(), "");
                deleted.setPath("/");
                deleted.setHttpOnly(true);
                deleted.setMaxAge(0);
                response.addCookie(deleted);
            }
        }
        log.info("Finish request logout");
    }
}
