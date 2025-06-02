package com.viettel.mycv.controller;

import com.viettel.mycv.common.ProjectTag;
import com.viettel.mycv.dto.request.ProjectCreateRequest;
import com.viettel.mycv.dto.request.ProjectUpdateRequest;
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
    public ResponseEntity<Object> getListProjects(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam List<ProjectTag> tags
    ) {
        log.info("Get request take list of projects");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "Get list of projects successful");
        result.put("data", projectService.findAll(pageNo, pageSize, tags));

        log.info("Finish getting list of projects");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody @Valid ProjectCreateRequest req) {
        log.info("Get request create profile for user with profile {}", req);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.CREATED.value());
        result.put("message", "Create project successfully");
        result .put("data", projectService.create(req));

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<Object> update(@RequestBody @Valid ProjectUpdateRequest req) {
        log.info("Get request update profile for user with profile {}", req);

        projectService.update(req);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.ACCEPTED.value());
        result.put("message", "Update project successfully");
        result.put("data", "");

        log.info("Update project successfully");
        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{projectId}")
    @PreAuthorize("hasAuthority({'manager', 'sysadmin'})")
    public ResponseEntity<Object> delete(@PathVariable @Min(value = 1, message="project id must greater than or equal to 1") Long projectId) {
        log.info("Get request delete project id = {}", projectId);

        projectService.delete(projectId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.NO_CONTENT.value());
        result.put("message", "Delete project successfully");
        result.put("data", "");

        log.info("Finish request delete project");
        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }
}
