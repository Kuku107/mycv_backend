package com.viettel.mycv.service;

import java.util.List;

public interface JwtService {
    String generateAccessToken(String email, List<String> authorities);
    String generateRefreshToken(String email, List<String> authorities);
    String extractEmail(String token, String keyType);
}
