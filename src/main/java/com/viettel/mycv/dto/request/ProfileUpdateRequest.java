package com.viettel.mycv.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ProfileUpdateRequest {
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
