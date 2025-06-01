package com.viettel.mycv.service;

import com.viettel.mycv.common.ProjectTag;
import com.viettel.mycv.dto.request.ProjectCreateRequest;
import com.viettel.mycv.dto.request.ProjectUpdateRequest;
import com.viettel.mycv.dto.response.ProjectDetailResponse;
import com.viettel.mycv.dto.response.ProjectPageResponse;

import java.util.List;

public interface ProjectService {
    Long create(ProjectCreateRequest req);

    void update(ProjectUpdateRequest req);

    void delete(Long projectId);

    ProjectPageResponse findAll(int pageNo, int pageSize, List<ProjectTag> tags);
}
