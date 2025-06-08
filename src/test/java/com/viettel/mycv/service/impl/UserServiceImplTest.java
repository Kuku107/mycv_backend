package com.viettel.mycv.service.impl;

import com.viettel.mycv.common.UserStatus;
import com.viettel.mycv.dto.request.UserCreateRequest;
import com.viettel.mycv.exception.ResourceNotFound;
import com.viettel.mycv.model.UserEntity;
import com.viettel.mycv.repository.ProfileRepository;
import com.viettel.mycv.repository.RoleRepository;
import com.viettel.mycv.repository.UserHasRoleRepository;
import com.viettel.mycv.repository.UserRepository;
import com.viettel.mycv.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserHasRoleRepository userHasRoleRepository;

    private UserCreateRequest reqUser;
    private UserEntity user;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        reqUser = UserCreateRequest.builder()
                .email("leduc1078@gmail.com")
                .fullName("Le Van Duc")
                .password("password")
                .build();

        user = new UserEntity();
        user.setId(2L);
        user.setEmail("leduc1078@gmail.com");
        user.setPassword("password");
        user.setUserStatus(UserStatus.NONE);
    }

    @Test
    void save() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        userService.save(reqUser);

        assertEquals(2L, user.getId());
    }

    @Test
    void updateStatus() {
        Long userId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        userService.updateStatus(userId, UserStatus.ACTIVE);

        assertEquals(UserStatus.ACTIVE, user.getUserStatus());
    }

    @Test
    void updateStatus_Failure() {
        Long userId = 555555L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFound exception = assertThrows(ResourceNotFound.class, () -> userService.updateStatus(userId, UserStatus.ACTIVE));

        assertEquals("User not exist", exception.getMessage());
        verify(userRepository, never()).save(any(UserEntity.class));
    }
}