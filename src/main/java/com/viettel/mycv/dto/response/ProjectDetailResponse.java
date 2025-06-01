package com.viettel.mycv.dto.response;

import com.viettel.mycv.common.ProjectTag;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDetailResponse {
    private String projectImageUrl;
    private ProjectTag tag;
    private String projectName;
    private String projectDemoUrl;
    private String projectRepoUrl;
}
