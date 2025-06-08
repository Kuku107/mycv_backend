package com.viettel.mycv.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import com.viettel.mycv.dto.request.ContactMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.cloud.storage.HttpMethod;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j(topic="GCS-SERVICE")
public class GCSService {

    private final Storage storage;

    @Value("${SENDGRID_EMAIL_ROOT}")
    private String rootEmail;

    @Value("${gcs.bucket-name}")
    private String bucketName;

    public String generateUploadUrl(String fileName, String contentType) {
        log.info("GENERATING upload url for {} {} ", fileName, contentType);

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                .setContentType(contentType)
                .build();

        URL url = storage.signUrl(blobInfo, 15, TimeUnit.MINUTES,
                Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                Storage.SignUrlOption.withContentType());

        log.info("Finished GENERATING upload url");

        return url.toString();
    }

    public void deteleFileByName(String fileName) {
        log.info("Deleting file " + fileName);
        storage.delete(bucketName, fileName);
        log.info("Deleted file " + fileName);
    }

    public void sendEmail(String accessToken, ContactMessageRequest req) throws GeneralSecurityException, IOException {
        log.info("Sending email from {} to {} ", req.getEmailFrom(), req.getAuthorEmail());
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Gmail service = new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("MyCV")
                .build();

        String rawMessage = buildRawMessage(req.getEmailFrom(), req.getAuthorEmail(), req.getSubject(), req.getMessage());
        Message message = new Message().setRaw(rawMessage);

        service.users().messages().send("me", message).execute();
        log.info("Finish sending email from {} to {} ", req.getEmailFrom(), req.getAuthorEmail());
    }

    private String buildRawMessage(String email, String authorEmail, String subject, String message) {
        StringBuilder builder = new StringBuilder();

        builder.append("From: ").append(email).append("\r\n")
                .append("To: ").append(authorEmail).append("\r\n")
                .append("Subject: ").append(subject).append("\r\n")
                .append("\r\n").append(message);

        return Base64.getUrlEncoder().encodeToString(builder.toString().getBytes(StandardCharsets.UTF_8));
    }
}
