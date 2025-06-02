package com.viettel.mycv.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SignInRequest {
    private String email;
    private String password;
}
