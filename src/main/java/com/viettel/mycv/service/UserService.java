package com.viettel.mycv.service;

import com.viettel.mycv.common.UserStatus;
import com.viettel.mycv.dto.request.UserCreateRequest;

public interface UserService {
    Long save(UserCreateRequest req);

    void updateStatus(Long userId, UserStatus userStatus);
}
