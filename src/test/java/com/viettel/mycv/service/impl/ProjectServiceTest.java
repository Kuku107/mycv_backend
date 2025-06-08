package com.viettel.mycv.service.impl;

import com.viettel.mycv.common.ProjectTag;
import com.viettel.mycv.dto.request.ProjectCreateRequest;
import com.viettel.mycv.dto.request.ProjectUpdateRequest;
import com.viettel.mycv.dto.response.ProjectPageResponse;
import com.viettel.mycv.model.ProjectEntity;
import com.viettel.mycv.repository.ProjectRepository;
import com.viettel.mycv.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private ProjectCreateRequest req;
    private ProjectEntity project;
    private ProjectUpdateRequest reqUpdate;

    @BeforeEach
    void beforeEach() {
        req = ProjectCreateRequest.builder()
                .name("project 1")
                .description("This is project 1")
                .projectImageUrl("url")
                .tag(ProjectTag.DESIGN)
                .projectDemoUrl("url")
                .projectRepoUrl("url")
                .build();

        project = new ProjectEntity();
        project.setId(1L);
        project.setUserId(1L);
        project.setTitle("title project 1");
        project.setDescription("description project 1");
        project.setProjectUrl("url");
        project.setGithubUrl("url");
        project.setThumbnailUrl("url");
        project.setTag(ProjectTag.DESIGN);

        reqUpdate = ProjectUpdateRequest.builder()
                .projectId(1L)
                .tag(ProjectTag.DESIGN)
                .projectDemoUrl("demo")
                .projectRepoUrl("repo")
                .name("project updated")
                .description("description project updated")
                .projectImageUrl("image url")
                .build();
    }

    @Test
    void create() {
        Long userId = 1L;
        when(authenticationService.getContextUserId()).thenReturn(userId);
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(project);

        Long result = projectService.create(req);

        assertEquals(userId, result);
    }

    @Test
    void update() {
        when(projectRepository.findById(any(Long.class))).thenReturn(Optional.of(project));
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(project);

        projectService.update(reqUpdate);

        assertEquals("project updated", project.getTitle());
    }

    @Test
    void delete() {
        Long userId = 1L;

        projectService.delete(userId);

        verify(projectRepository, times(1)).deleteById(userId);
    }

    @Test
    void findAll() {
        Long userId = 1L;
        List<ProjectTag> tags = List.of(ProjectTag.SHOW_ALL);

        ProjectEntity project1 = new ProjectEntity();
        project1.setId(1L);
        project1.setUserId(1L);
        project1.setTitle("title project 1");
        project1.setDescription("description project 1");
        project1.setProjectUrl("url");
        project1.setGithubUrl("url");
        project1.setThumbnailUrl("url");
        project1.setTag(ProjectTag.DESIGN);

        ProjectEntity project2 = new ProjectEntity();
        project2.setId(2L);
        project2.setUserId(1L);
        project2.setTitle("title project 1");
        project2.setDescription("description project 1");
        project2.setProjectUrl("url");
        project2.setGithubUrl("url");
        project2.setThumbnailUrl("url");
        project2.setTag(ProjectTag.DESIGN);

        Page<ProjectEntity> pageProjectEntity = new PageImpl<>(List.of(project1, project2));

        when(projectRepository.findAllByUserId(any(Long.class), any(Pageable.class))).thenReturn(pageProjectEntity);

        ProjectPageResponse result = projectService.findAll(0, 2, tags, userId);

        assertNotNull(result);

        assertEquals(1, result.getTotalPages());
    }
}