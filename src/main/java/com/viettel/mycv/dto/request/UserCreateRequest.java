package com.viettel.mycv.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserCreateRequest {
    @NotBlank(message = "user have to fill fullName")
    private String fullName;

    @Email(message = "email invalid")
    private String email;

    @NotBlank(message = "password must not be blank")
    private String password;
}
