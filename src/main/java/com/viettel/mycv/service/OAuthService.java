package com.viettel.mycv.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.viettel.mycv.dto.request.ContactMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j(topic="OAUTH-SERVICE")
public class OAuthService {

    @Value("${REDIRECT_URL}")
    private String redirectUrl;

    private final List<String> scopes = List.of("https://www.googleapis.com/auth/gmail.send");

    private final Map<String, ContactMessageRequest> pendingEmails = new ConcurrentHashMap<>();

    private GoogleAuthorizationCodeFlow flow;

    public OAuthService() throws IOException, GeneralSecurityException {
        var jsonFactory = JacksonFactory.getDefaultInstance();
        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        var inputStream = getClass().getResourceAsStream("/gcs/client_secret_google_mail.json");

        if (inputStream == null) {
            throw new IOException("input Stream is null");
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(inputStream));

        this.flow = new GoogleAuthorizationCodeFlow
                .Builder(httpTransport, jsonFactory, clientSecrets, this.scopes)
                .setDataStoreFactory(new MemoryDataStoreFactory())
                .setAccessType("online")
                .build();
    }

    public String buildAuthUrl(String state, String loginHint) {
        return flow.newAuthorizationUrl()
                .setRedirectUri(this.redirectUrl)
                .setState(state)
                .set("login_hint", loginHint)
                .build();
    }

    public String getAccessTokenFromCode(String code) throws IOException {
        var tokenResponse = flow.newTokenRequest(code)
                .setRedirectUri(this.redirectUrl)
                .execute();

        return tokenResponse.getAccessToken();
    }

    public void saveState(String state, ContactMessageRequest pendingEmail) {
        pendingEmails.put(state, pendingEmail);
    }

    public ContactMessageRequest getContactMessageRequestByState(String state) {
        return pendingEmails.remove(state);
    }
}
