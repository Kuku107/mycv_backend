package com.viettel.mycv.dto.request;

import lombok.Getter;

@Getter
public class ContactMessageRequest {
    private String authorEmail;
    private String emailFrom;
    private String subject;
    private String message;
}
