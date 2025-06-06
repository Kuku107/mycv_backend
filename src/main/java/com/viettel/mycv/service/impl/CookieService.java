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
//        Cookie accessCookie = new Cookie("access-token", accessToken);
//        accessCookie.setHttpOnly(true);
//        accessCookie.setSecure(false);
//        accessCookie.setPath("/");
//        accessCookie.setMaxAge(60 * 15);
//        response.addCookie(accessCookie);

        String cookie = "access-token=" + accessToken +
                "; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=900";
        response.addHeader("Set-Cookie", cookie);
    }

    public void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
//        Cookie refreshCookie = new Cookie("refresh-token", refreshToken);
//        refreshCookie.setHttpOnly(true);
//        refreshCookie.setSecure(true);
//        refreshCookie.setPath("/");
//        refreshCookie.setMaxAge(60 * 60 * 24 * 7);
//        response.addCookie(refreshCookie);

        String cookie = "refresh-token=" + refreshToken + "; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=604800";
        response.addHeader("Set-Cookie", cookie);
    }
}
