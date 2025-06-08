package com.viettel.mycv.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse {
    private int status;
    private String message;
    private Object data;
}
