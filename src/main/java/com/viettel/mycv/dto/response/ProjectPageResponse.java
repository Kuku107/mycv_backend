package com.viettel.mycv.dto.response;

import lombok.*;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectPageResponse {
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private List<ProjectDetailResponse> projects;
}
