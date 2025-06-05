package com.viettel.mycv.controller;


import com.viettel.mycv.dto.request.ProfileUpdateRequest;
import com.viettel.mycv.dto.response.ProfileResponse;
import com.viettel.mycv.model.ProfileEntity;
import com.viettel.mycv.model.UserEntity;
import com.viettel.mycv.service.JwtService;
import com.viettel.mycv.service.ProfileService;
import com.viettel.mycv.service.impl.CookieService;
import com.viettel.mycv.service.impl.ProfileServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("")
    public ResponseEntity<Object> getProfile(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam(required = false, name = "userId") Long reqUserId) {

        Long userId = reqUserId;
        if (user != null) {
            userId = user.getId();
        }

        log.info("Get request to get profile for userid {}", userId);

        ProfileEntity profileEntity = profileService.findByUserId(userId);
        ProfileResponse response = ProfileResponse.builder()
                .bio(profileEntity.getBio())
                .profileUrl(profileEntity.getProfileImageUrl())
                .phone(profileEntity.getPhone())
                .name(profileEntity.getName())
                .email((user != null) ? user.getEmail() : "")
                .address(profileEntity.getAddress())
                .instagramUrl(profileEntity.getInstagramUrl())
                .facebookUrl(profileEntity.getFacebookUrl())
                .twitterUrl(profileEntity.getTwitterUrl())
                .jobTitle(profileEntity.getJobTitle())
                .build();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "get profile successfully");
        result.put("data", response);

        log.info("Finish get request to get profile for userid {}", userId);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

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
