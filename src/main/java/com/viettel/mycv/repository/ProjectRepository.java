package com.viettel.mycv.repository;

import com.viettel.mycv.common.ProjectTag;
import com.viettel.mycv.dto.response.ProjectDetailResponse;
import com.viettel.mycv.model.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Page<ProjectEntity> findByTagIn(List<ProjectTag> tags, Pageable pageable);
}
