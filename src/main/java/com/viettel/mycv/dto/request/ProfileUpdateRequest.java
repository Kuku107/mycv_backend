package com.viettel.mycv.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProfileUpdateRequest {
    @NotNull(message = "userId could not be null")
    @Min(value = 1, message = "userId have to greater than or equal to 1")
    private Long userId;

    private String jobTitle;

    @NotBlank(message = "The profile have to have a name")
    private String name;
    private String phone;
    private String address;
    private String bio;
    private String profileUrl;
    private String facebookUrl;
    private String twitterUrl;
    private String instagramUrl;
}
