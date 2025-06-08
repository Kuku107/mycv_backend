package com.viettel.mycv.controller;

import com.viettel.mycv.common.ProjectTag;
import com.viettel.mycv.config.Translator;
import com.viettel.mycv.dto.request.ProjectCreateRequest;
import com.viettel.mycv.dto.request.ProjectUpdateRequest;
import com.viettel.mycv.dto.response.ApiResponse;
import com.viettel.mycv.dto.response.ProjectPageResponse;
import com.viettel.mycv.model.ProjectEntity;
import com.viettel.mycv.service.ProjectService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/project")
@Slf4j(topic="PROJECT CONTROLLER")
@RequiredArgsConstructor
@Validated
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("")
    public ApiResponse getListProjects(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(required = false) List<ProjectTag> tags,
            @RequestParam(required = false) Long userId
    ) {
        log.info("Get request take list of projects");

        ProjectPageResponse result = projectService.findAll(pageNo, pageSize, tags, userId);

        log.info("Finish getting list of projects");

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("project.get.success"))
                .data(result)
                .build();
    }

    @PostMapping("")
    public ApiResponse create(@RequestBody @Valid ProjectCreateRequest req) {
        log.info("Get request create profile for user with profile {}", req);

        Long userId = projectService.create(req);

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(Translator.translate("project.create.success"))
                .data(userId)
                .build();
    }

    @PutMapping("")
    public ApiResponse update(@RequestBody @Valid ProjectUpdateRequest req) {
        log.info("Get request update profile for user with profile {}", req);

        projectService.update(req);

        log.info("Update project successfully");

        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message(Translator.translate("project.update.success"))
                .build();
    }

    @DeleteMapping("{projectId}")
    @PreAuthorize("hasAnyAuthority('manager', 'sysadmin')")
    public ApiResponse delete(@PathVariable @Min(value = 1, message="project id must greater than or equal to 1") Long projectId) {
        log.info("Get request delete project id = {}", projectId);

        projectService.delete(projectId);

        log.info("Finish request delete project");

        return ApiResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(Translator.translate("project.delete.success"))
                .build();
    }
}
