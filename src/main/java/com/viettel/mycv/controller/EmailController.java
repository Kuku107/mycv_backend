package com.viettel.mycv.controller;

import com.viettel.mycv.common.UserStatus;
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
    public ResponseEntity<Object> sendEmail(@RequestParam String to,
                                            @RequestParam String subject,
                                            @RequestParam String body) {
        log.info("Get request sending email to " + to);

        emailService.sendEmail(to, subject, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "Sent email successfully");
        result.put("data", "");
        log.info("Finished sending email to " + to);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/verification-email")
    public ResponseEntity<Object> verificationEmail(@RequestParam String to, @RequestParam String name, @RequestParam Long userId) {
        log.info("Get reuqest verification email to " + to);
        emailService.verificationEmail(to, name, userId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "Verification email successfully");
        result.put("data", "");

        log.info("Finished verification email");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<Object> verifyEmail(@RequestParam String token) {
        log.info("Get request confirm email to " + token);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = Long.valueOf(claims.getSubject());
            userService.updateStatus(userId, UserStatus.ACTIVE);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("status", HttpStatus.ACCEPTED.value());
            result.put("message", "Verify email successfully");
            result.put("data", "");
            log.info("Finished confirm email");

            return new ResponseEntity<>(result, HttpStatus.ACCEPTED);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }
}
