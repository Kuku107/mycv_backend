package com.viettel.mycv.service.impl;

import com.viettel.mycv.common.ProjectTag;
import com.viettel.mycv.dto.request.ProjectCreateRequest;
import com.viettel.mycv.dto.request.ProjectUpdateRequest;
import com.viettel.mycv.dto.response.ProjectDetailResponse;
import com.viettel.mycv.dto.response.ProjectPageResponse;
import com.viettel.mycv.exception.ResourceNotFound;
import com.viettel.mycv.model.ProjectEntity;
import com.viettel.mycv.model.UserEntity;
import com.viettel.mycv.repository.ProjectRepository;
import com.viettel.mycv.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        long userId = user.getId();

        ProjectEntity project = new ProjectEntity();
        project.setThumbnailUrl(req.getProjectImageUrl());
        project.setTitle(req.getName());
        project.setUserId(userId);
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
    public ProjectPageResponse findAll(int pageNo, int pageSize, List<ProjectTag> tags, Long reqUserId) {
        log.info("Finding projects by pageNo: {}, pageSize: {}", pageNo, pageSize);

        Long userId = reqUserId;

        if (userId == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserEntity user = (UserEntity) authentication.getPrincipal();
            userId = user.getId();
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<ProjectEntity> pageProjectEntity;

        if (tags == null || tags.isEmpty()) {
            return ProjectPageResponse.builder()
                    .pageNo(pageNo)
                    .pageSize(pageSize)
                    .totalPages(0)
                    .projects(new ArrayList<>())
                    .build();
        }

        if (tags.contains(ProjectTag.SHOW_ALL)) {
            pageProjectEntity = projectRepository.findAllByUserId(userId, pageable);
        } else {
            pageProjectEntity = projectRepository.findByTagInAndUserId(tags, userId, pageable);
        }

        log.info("Found {} projects", pageProjectEntity.getTotalElements());
        return convertToProjectPageResponse(pageNo, pageSize, pageProjectEntity);
    }

    private ProjectPageResponse convertToProjectPageResponse(int pageNo, int pageSize, Page<ProjectEntity> pageProjectEntity) {
        List<ProjectDetailResponse> projectDetailResponses = pageProjectEntity.stream()
                .map(entity -> ProjectDetailResponse.builder()
                                                .projectImageUrl(entity.getThumbnailUrl())
                                                .tag(entity.getTag())
                                                .id(entity.getId())
                                                .projectName(entity.getTitle())
                                                .projectDemoUrl(entity.getProjectUrl())
                                                .projectRepoUrl(entity.getGithubUrl())
                                                .description(entity.getDescription())
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
