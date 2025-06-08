package com.viettel.mycv;

import com.viettel.mycv.repository.ProfileRepository;
import com.viettel.mycv.repository.ProjectRepository;
import com.viettel.mycv.repository.UserRepository;
import com.viettel.mycv.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MycvApplicationTests {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    void loadContext() {
        Assertions.assertNotNull(authenticationService);
        Assertions.assertNotNull(projectService);
        Assertions.assertNotNull(profileService);
        Assertions.assertNotNull(userService);
        Assertions.assertNotNull(jwtService);
        Assertions.assertNotNull(projectRepository);
        Assertions.assertNotNull(profileRepository);
        Assertions.assertNotNull(userRepository);
    }
}
