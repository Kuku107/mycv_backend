package com.viettel.mycv.config;

import ch.qos.logback.core.util.StringUtil;
import com.viettel.mycv.model.UserEntity;
import com.viettel.mycv.service.JwtService;
import com.viettel.mycv.service.MyUserDetailsService;
import com.viettel.mycv.service.UserService;
import com.viettel.mycv.service.impl.CookieService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic="CUSTOMIZE_REQUEST_FILTER")
@Component
@RequiredArgsConstructor
public class CustomizeRequestFilter extends OncePerRequestFilter {

    private final CookieService cookieService;
    private final JwtService jwtService;
    private final MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("{} {}", request.getMethod(), request.getRequestURI());

        String tokenType = "access-token";
        String keyType = "access-key";

        if (request.getRequestURI().equals("/auth/refresh-key")) {
            tokenType = "refresh-token";
            keyType="refresh-key";
        }

        String token = cookieService.getTokenByName(request, tokenType);
        log.info("token type: {}", tokenType);

        if (StringUtils.hasLength(token)) {
            try {
                String email = jwtService.extractEmail(token, keyType);
                UserEntity user = myUserDetailsService.loadUserByUsername(email);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetails(request));
                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);
            }  catch (ExpiredJwtException e) {
                log.warn("Access token expired: {}", e.getMessage());

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String json = String.format(
                        "{\"status\": 401, \"message\": \"Access token expired\", \"path\": \"%s\"}",
                        request.getRequestURI()
                );

                response.getWriter().write(json);
                return;
            } catch (UsernameNotFoundException e) {
                log.warn("User not found: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                return;
            }

            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);
        }

    }
}
