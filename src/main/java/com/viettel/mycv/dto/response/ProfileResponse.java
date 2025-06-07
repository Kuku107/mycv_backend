package com.viettel.mycv.dto.response;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProfileResponse {
    private Long userId;
    private String jobTitle;
    private String email;
    private String name;
    private String phone;
    private String address;
    private String bio;
    private String profileUrl;
    private String facebookUrl;
    private String twitterUrl;
    private String instagramUrl;
}
