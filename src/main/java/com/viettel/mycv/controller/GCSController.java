package com.viettel.mycv.controller;

import com.viettel.mycv.config.Translator;
import com.viettel.mycv.dto.request.ContactMessageRequest;
import com.viettel.mycv.service.GCSService;
import com.viettel.mycv.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j(topic="GCS-CONTROLLER")
public class GCSController {

    private final GCSService gcsService;

    private final OAuthService oAuthService;

    @PostMapping("/api/images/generate-upload-url")
    public ResponseEntity<Map<String, String>> generateUploadUrl(
            @RequestParam String fileName,
            @RequestParam String contentType) {

        log.info("Getting REQUEST to generate upload url for " + fileName);

        String signedUrl = gcsService.generateUploadUrl(fileName, contentType);

        log.info("Finished REQUEST generating upload url");
        return ResponseEntity.ok(Map.of("uploadUrl", signedUrl));
    }

    @DeleteMapping("/api/images/delete")
    public ResponseEntity<Object> delete(@RequestParam String fileName) {
        log.info("Get REQUEST deleting file " + fileName);
        gcsService.deteleFileByName(fileName);
        log.info("Finish REQUEST delete file " + fileName);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auth/google")
    public ResponseEntity<Object> verifyByGoogle(@RequestBody ContactMessageRequest req) throws IOException {
        log.info("Get request verifying google email");

        String state = UUID.randomUUID().toString();
        oAuthService.saveState(state, req);
        String url = oAuthService.buildAuthUrl(state, req.getEmailFrom());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.ACCEPTED.value());
        result.put("message", Translator.translate("google.auth.url.create.success"));
        result.put("data", Map.of("authUrl", url));

        log.info("Finished request verifying google email");
        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }

    @GetMapping("/oauth2/callback")
    public ResponseEntity<Object> handleCallback(@RequestParam String code, @RequestParam String state) {
        log.info("Get request verifying google code callback");
        try {
            String accessToken = oAuthService.getAccessTokenFromCode(code);
            ContactMessageRequest req = oAuthService.getContactMessageRequestByState(state);

            gcsService.sendEmail(accessToken, req);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Finished request verifying google code callback");
        return ResponseEntity.noContent().build();
    }
}
