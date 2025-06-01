package com.viettel.mycv.service.impl;

import com.viettel.mycv.common.ProjectTag;
import com.viettel.mycv.dto.request.ProjectCreateRequest;
import com.viettel.mycv.dto.request.ProjectUpdateRequest;
import com.viettel.mycv.dto.response.ProjectDetailResponse;
import com.viettel.mycv.dto.response.ProjectPageResponse;
import com.viettel.mycv.exception.ResourceNotFound;
import com.viettel.mycv.model.ProjectEntity;
import com.viettel.mycv.repository.ProjectRepository;
import com.viettel.mycv.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "PROJECT SERVICE")
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public Long create(ProjectCreateRequest req) {
        log.info("Creating project: {}", req);

        ProjectEntity project = new ProjectEntity();
        project.setThumbnailUrl(req.getProjectImageUrl());
        project.setTitle(req.getName());
        project.setUserId(req.getUserId());
        project.setTag(req.getTag());
        project.setProjectUrl(req.getProjectDemoUrl());
        project.setGithubUrl(req.getProjectRepoUrl());
        project.setDescription(req.getDescription());

        projectRepository.save(project);
        log.info("Project created: {}", project);
        return project.getId();
    }

    @Override
    public void update(ProjectUpdateRequest req) {
        log.info("Updating project: {}", req);

        ProjectEntity project = getProjectById(req.getProjectId());
        project.setThumbnailUrl(req.getProjectImageUrl());
        project.setTitle(req.getName());
        project.setTag(req.getTag());
        project.setDescription(req.getDescription());
        project.setProjectUrl(req.getProjectDemoUrl());
        project.setGithubUrl(req.getProjectRepoUrl());

        projectRepository.save(project);
        log.info("Project updated: {}", project);
    }

    @Override
    public void delete(Long projectId) {
        log.info("Deleting project: {}", projectId);
        projectRepository.deleteById(projectId);
        log.info("Project deleted: {}", projectId);
    }

    @Override
    public ProjectPageResponse findAll(int pageNo, int pageSize, List<ProjectTag> tags) {
        log.info("Finding projects by pageNo: {}, pageSize: {}", pageNo, pageSize);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<ProjectEntity> pageProjectEntity;
        if (tags.contains(ProjectTag.SHOW_ALL)) {
            pageProjectEntity = projectRepository.findAll(pageable);
        } else {
            pageProjectEntity = projectRepository.findByTagIn(tags, pageable);
        }

        log.info("Found {} projects", pageProjectEntity.getTotalElements());
        return convertToProjectPageResponse(pageNo, pageSize, pageProjectEntity);
    }

    private ProjectPageResponse convertToProjectPageResponse(int pageNo, int pageSize, Page<ProjectEntity> pageProjectEntity) {
        List<ProjectDetailResponse> projectDetailResponses = pageProjectEntity.stream()
                .map(entity -> ProjectDetailResponse.builder()
                                                .projectImageUrl(entity.getThumbnailUrl())
                                                .tag(entity.getTag())
                                                .projectName(entity.getTitle())
                                                .projectDemoUrl(entity.getProjectUrl())
                                                .projectRepoUrl(entity.getGithubUrl())
                                                .build())
                .toList();

        return ProjectPageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(pageProjectEntity.getTotalPages())
                .projects(projectDetailResponses)
                .build();
    }

    private ProjectEntity getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Project not found with id: " + id));
    }
}
