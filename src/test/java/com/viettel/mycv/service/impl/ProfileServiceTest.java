package com.viettel.mycv.service.impl;

import com.viettel.mycv.dto.request.ProfileUpdateRequest;
import com.viettel.mycv.exception.ResourceNotFound;
import com.viettel.mycv.model.ProfileEntity;
import com.viettel.mycv.repository.ProfileRepository;
import com.viettel.mycv.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private ProfileUpdateRequest req;

    private ProfileEntity profile;

    @BeforeEach
    void setUp() {
        req = ProfileUpdateRequest.builder()
                .jobTitle("Job Title")
                .name("Duc")
                .phone("123456789")
                .address("Address")
                .bio("bio")
                .profileUrl("profileUrl")
                .facebookUrl("facebookUrl")
                .twitterUrl("twitterUrl")
                .instagramUrl("instagramUrl")
                .build();

        profile = new ProfileEntity();
        profile.setUserId(1L);
        profile.setJobTitle("initial");
        profile.setName("initial");
        profile.setPhone("initial");
        profile.setAddress("initial");
        profile.setBio("bio");
        profile.setFacebookUrl("initial");
        profile.setTwitterUrl("initial");
        profile.setInstagramUrl("initial");
        profile.setProfileImageUrl("initial");
    }

    @Test
    void findByUserId_success() {
        Long userId = 1L;
        when (profileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));

        ProfileEntity result = profileService.findByUserId(userId);

        assertNotNull(result);
        assertEquals("initial", result.getName());
    }

    @Test
    void update() {
        Long userId = 1L;
        when(profileRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(profile));
        when(authenticationService.getContextUserId()).thenReturn(userId);

        profileService.update(req);

        assertNotNull(profile);
        assertEquals("Duc", profile.getName());
    }

    @Test
    void findByUserId_Failure() {
        Long userId = 100L;

        ResourceNotFound exception = assertThrows(ResourceNotFound.class, () -> profileService.findByUserId(userId));

        assertEquals("profile not exist", exception.getMessage());
    }
}