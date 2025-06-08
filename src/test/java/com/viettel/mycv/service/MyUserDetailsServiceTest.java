package com.viettel.mycv.service;

import com.viettel.mycv.common.UserStatus;
import com.viettel.mycv.model.UserEntity;
import com.viettel.mycv.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService service;

    private static UserEntity user;

    @BeforeAll
    static void beforeAll() {
        user = new UserEntity();
        user.setId(1L);
        user.setUserStatus(UserStatus.ACTIVE);
        user.setEmail("leduc1078@gmail.com");
        user.setPassword("password");
    }

    @Test
    void loadUserByUsername() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(user);

        UserEntity result = service.loadUserByUsername("leduc1078@gmail.com");
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
}