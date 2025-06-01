package com.viettel.mycv.service.impl;

import com.viettel.mycv.dto.request.ProfileUpdateRequest;
import com.viettel.mycv.exception.ResourceNotFound;
import com.viettel.mycv.model.ProfileEntity;
import com.viettel.mycv.repository.ProfileRepository;
import com.viettel.mycv.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic="PROFILE SERVICE")
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    public void update(ProfileUpdateRequest req) {
        log.info("Updating profile");
        ProfileEntity profile = getProfileByUserId(req.getUserId());
        profile.setJobTitle(req.getJobTitle());
        profile.setName(req.getName());
        profile.setPhone(req.getPhone());
        profile.setAddress(req.getAddress());
        profile.setBio(req.getBio());
        profile.setProfileImageUrl(req.getProfileUrl());
        profile.setFacebookUrl(req.getFacebookUrl());
        profile.setInstagramUrl(req.getInstagramUrl());
        profile.setTwitterUrl(req.getTwitterUrl());
        log.info("Profile updated");

        profileRepository.save(profile);
    }

    private ProfileEntity getProfileByUserId(Long id) {
        return profileRepository.findByUserId(id).orElseThrow(() -> new ResourceNotFound("profile not exist"));
    }
}
