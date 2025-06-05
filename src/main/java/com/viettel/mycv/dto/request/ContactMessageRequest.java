package com.viettel.mycv.dto.request;

import lombok.Getter;

@Getter
public class ContactMessageRequest {
    private String email;
    private String subject;
    private String message;
}
