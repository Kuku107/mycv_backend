package com.viettel.mycv.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j(topic="EMAIL-SERVICE")
@RequiredArgsConstructor
public class EmailService {

    @Value("${SENDGRID_EMAIL_NO_REPLY}")
    private String from;

    private final SendGrid sendGrid;

    @Value("${TEMPLATE_ID}")
    private String templateId;

    @Value("${VERIFY_KEY}")
    private String secretKey;

    @Value("${BASE_VERIFICATION_LINK}")
    private String baseUrlVerificationLink;

    public void sendEmail(String to, String subject, String body) {
        log.info("Sending email to " + to);

        Email emailFrom = new Email(from, "MyCV");
        Email emailTo = new Email(to);

        Content content = new Content("text/plain", body);

        Mail mail = new Mail(emailFrom, subject, emailTo, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        try {
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            if (response.getStatusCode() == 202) {
                log.info("Email sent to " + to + "successfully");
            }
        } catch (IOException e) {
            log.error("Error sending email to {} {}", to, e.getMessage());
        }
    }

    public void verificationEmail(String to, String name, Long userId) {
        Email emailFrom = new Email(from, "MyCV");
        Email emailTo = new Email(to);
        String token = generateEmailVerificationToken(userId);
        String verificationUrl = baseUrlVerificationLink + token;

        Mail mail = new Mail();
        mail.setFrom(emailFrom);
        mail.setTemplateId(templateId);

        Map<String, String> data = new HashMap<>();
        data.put("name", name);
        data.put("verification_link", verificationUrl);

        Personalization personalization = new Personalization();
        personalization.addTo(emailTo);
        data.forEach(personalization::addDynamicTemplateData);

        mail.addPersonalization(personalization);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        try {
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            if (response.getStatusCode() == 202) {
                log.info("Email sent to " + to + "successfully");
            }
        } catch (IOException e) {
            log.error("Error sending email to {} {}", to, e.getMessage());
        }

    }

    public String generateEmailVerificationToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 1 * 60 * 1000);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
