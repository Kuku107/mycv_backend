package com.viettel.mycv.controller;


import com.viettel.mycv.config.Translator;
import com.viettel.mycv.dto.request.ProfileUpdateRequest;
import com.viettel.mycv.dto.response.ApiResponse;
import com.viettel.mycv.dto.response.ProfileResponse;
import com.viettel.mycv.model.ProfileEntity;
import com.viettel.mycv.model.UserEntity;
import com.viettel.mycv.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/profile")
@Slf4j(topic="PROFILE CONTROLLER")
@RequiredArgsConstructor
@Validated
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("")
    public ApiResponse getProfile(
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
                .userId(userId)
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


        log.info("Finish get request to get profile for userid {}", userId);
        log.info("Hom nay troi dep");

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("profile.get.success"))
                .data(response)
                .build();

    }

    @PutMapping("")
    public ApiResponse update(@RequestBody @Valid ProfileUpdateRequest req) {
        log.info("Get request update profile for user with profile {}", req);

        profileService.update(req);
        log.info("Finished update profile for user {} with profile", req);

        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message(Translator.translate("profile.update.success"))
                .build();
    }
}
