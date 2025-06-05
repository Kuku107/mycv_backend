package com.viettel.mycv.service.impl;

import com.viettel.mycv.common.UserStatus;
import com.viettel.mycv.dto.request.UserCreateRequest;
import com.viettel.mycv.exception.ResourceNotFound;
import com.viettel.mycv.model.ProfileEntity;
import com.viettel.mycv.model.RoleEntity;
import com.viettel.mycv.model.UserEntity;
import com.viettel.mycv.model.UserHasRole;
import com.viettel.mycv.repository.ProfileRepository;
import com.viettel.mycv.repository.RoleRepository;
import com.viettel.mycv.repository.UserHasRoleRepository;
import com.viettel.mycv.repository.UserRepository;
import com.viettel.mycv.service.EmailService;
import com.viettel.mycv.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic="USER SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final UserHasRoleRepository userHasRoleRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Long save(UserCreateRequest req) {
        log.info("saving user {}", req);

        UserEntity user = new UserEntity();
        ProfileEntity profile = new ProfileEntity();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setUserStatus(UserStatus.NONE);
        profile.setName(req.getFullName());

//      Lưu UserEntity và ProfileEntity
        user = userRepository.save(user);
        log.info("saved user {}", user);
        profile.setUserId(user.getId());
        profileRepository.save(profile);

//      Lưu Role của user - manager
        UserHasRole userHasRole = new UserHasRole();

        RoleEntity role = roleRepository.findByName("manager");
        userHasRole.setUser(user);
        userHasRole.setRole(role);
        userHasRoleRepository.save(userHasRole);


        emailService.verificationEmail(req.getEmail(), req.getFullName(), user.getId());
        log.info("saved profile {}", profile);

        return user.getId();
    }

    @Override
    public void updateStatus(Long userId, UserStatus userStatus) {
        log.info("updating status of user {}", userId);
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User not exist"));
        user.setUserStatus(userStatus);
        userRepository.save(user);
        log.info("updated status of user {}", userId);
    }
}
