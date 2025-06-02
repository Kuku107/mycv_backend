package com.viettel.mycv.service.impl;

import com.viettel.mycv.dto.request.SignInRequest;
import com.viettel.mycv.dto.response.TokenResponse;
import com.viettel.mycv.repository.UserRepository;
import com.viettel.mycv.service.AuthenticationService;
import com.viettel.mycv.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j(topic="AUTHENTICATION SERVICE")
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public TokenResponse getToken(SignInRequest request) {
        log.info("Creating new access token and refresh token");

        List<String> authorities = new ArrayList<>();
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            log.info("authenticated: {}", authentication.isAuthenticated());
            log.info("authorities: {}", authentication.getAuthorities());
            authentication.getAuthorities().forEach(authority -> authorities.add(authority.getAuthority()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException | DisabledException e) {
            log.error("errorMessage: {}", e.getMessage());
            throw new AccessDeniedException(e.getMessage());
        }
        String accessToken = jwtService.generateAccessToken(request.getEmail(), authorities);
        String refreshToken = jwtService.generateRefreshToken(request.getEmail(), authorities);

        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        String email = jwtService.extractEmail(refreshToken, "refresh-key");

        var user = userRepository.findByEmail(email);

        List<String> authorities = new ArrayList<>();
        user.getAuthorities().forEach(authority -> authorities.add(authority.getAuthority()));
        String accessKey = jwtService.generateAccessToken(user.getEmail(), authorities);

        return TokenResponse.builder().accessToken(accessKey).refreshToken(refreshToken).build();
    }
}
