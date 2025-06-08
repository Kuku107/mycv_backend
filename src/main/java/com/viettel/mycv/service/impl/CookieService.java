package com.viettel.mycv.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic="COOKIE-SERVICE")
public class CookieService {

    public String getTokenByName(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void addAccessTokenToCookie(HttpServletResponse response, String accessToken) {

        String cookie = "access-token=" + accessToken +
                "; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=900";
        response.addHeader("Set-Cookie", cookie);
    }

    public void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {

        String cookie = "refresh-token=" + refreshToken + "; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=604800";
        response.addHeader("Set-Cookie", cookie);
    }
}
