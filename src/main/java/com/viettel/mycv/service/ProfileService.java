package com.viettel.mycv.service;

import com.viettel.mycv.dto.request.ProfileUpdateRequest;
import com.viettel.mycv.model.ProfileEntity;

public interface ProfileService {
    void update(ProfileUpdateRequest req);
    ProfileEntity findByUserId(Long id);
}
