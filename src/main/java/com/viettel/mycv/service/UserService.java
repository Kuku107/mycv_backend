package com.viettel.mycv.service;

import com.viettel.mycv.common.UserStatus;
import com.viettel.mycv.dto.request.UserCreateRequest;
import com.viettel.mycv.model.UserEntity;

public interface UserService {
    Long save(UserCreateRequest req);

    void updateStatus(Long userId, UserStatus userStatus);
}
