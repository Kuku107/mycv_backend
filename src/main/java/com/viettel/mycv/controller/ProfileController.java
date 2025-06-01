package com.viettel.mycv.controller;


import com.viettel.mycv.dto.request.ProfileUpdateRequest;
import com.viettel.mycv.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/profile")
@Slf4j(topic="PROFILE CONTROLLER")
@RequiredArgsConstructor
@Validated
public class ProfileController {
    private final ProfileService profileService;

    @PutMapping("")
    public ResponseEntity<Object> update(@RequestBody @Valid ProfileUpdateRequest req) {
        log.info("Get request update profile for user with profile {}", req);

        profileService.update(req);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.ACCEPTED.value());
        result.put("message", "profile updated successfully");
        result.put("data", "");

        log.info("Finished update profile for user {} with profile", req);

        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }
}
