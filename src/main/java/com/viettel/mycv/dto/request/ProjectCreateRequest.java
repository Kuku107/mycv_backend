package com.viettel.mycv.dto.request;

import com.viettel.mycv.common.ProjectTag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ProjectCreateRequest {
    @NotBlank(message = "The project have to have a name")
    private String name;
    private String description;
    private String projectImageUrl;

    @NotNull(message = "The project have to have the tag")
    private ProjectTag tag;
    private String projectDemoUrl;
    private String projectRepoUrl;
}
