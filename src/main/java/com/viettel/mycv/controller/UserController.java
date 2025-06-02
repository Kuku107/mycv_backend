package com.viettel.mycv.controller;

import com.viettel.mycv.dto.request.UserCreateRequest;
import com.viettel.mycv.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j(topic="USER CONTROLLER")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody @Valid UserCreateRequest req) {
        log.info("Creating user: {}", req);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.CREATED.value());
        result.put("message", "Create user successful");
        result.put("data", userService.save(req));

        log.info("Finish creating user");

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
