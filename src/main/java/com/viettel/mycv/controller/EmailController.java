package com.viettel.mycv.controller;

import com.viettel.mycv.common.UserStatus;
import com.viettel.mycv.config.Translator;
import com.viettel.mycv.dto.response.ApiResponse;
import com.viettel.mycv.model.UserEntity;
import com.viettel.mycv.service.EmailService;
import com.viettel.mycv.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@Slf4j(topic="EMAIL-CONTROLLER")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    private final UserService  userService;

    @Value("${JWT_VERIFY_KEY}")
    private String secretKey;

    @GetMapping("/send-email")
    public ApiResponse sendEmail(@RequestParam String to,
                                            @RequestParam String subject,
                                            @RequestParam String body) {
        log.info("Get request sending email to " + to);

        emailService.sendEmail(to, subject, body);
        log.info("Finished sending email to " + to);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("sendgrid.send.success"))
                .build();
    }

    @GetMapping("/verification-email")
    public ApiResponse verificationEmail(@RequestParam String to, @RequestParam String name, @RequestParam Long userId) {
        log.info("Get reuqest send verification email to " + to);
        emailService.verificationEmail(to, name, userId);
        log.info("Finished send verification email");

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("sendgrid.email.send.success"))
                .build();
    }

    @GetMapping("/confirm-email")
    public ApiResponse verifyEmail(@RequestParam String token) {
        log.info("Get request confirm email to " + token);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = Long.valueOf(claims.getSubject());
            userService.updateStatus(userId, UserStatus.ACTIVE);

            log.info("Finished confirm email");

            return ApiResponse.builder()
                    .status(HttpStatus.ACCEPTED.value())
                    .message(Translator.translate("sendgrid.email.confirm.sucess"))
                    .build();

        } catch (ExpiredJwtException e) {
            return ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message(Translator.translate("sendgrid.email.confirm.jwt.expire"))
                    .build();
        } catch (Exception e) {
            return ApiResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
        }
    }
}
