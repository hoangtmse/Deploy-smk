package com.swd.smk.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.ConferenceSolutionKey;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
public class GoogleMeetService {
    @Autowired
    private GoogleOAuthProperties props;

    // Hardcoded refresh token for testing - replace with DB or secure storage
    @Value("${google.oauth.refresh-token}")
    private String refreshToken;

    public String getAuthorizationUrl() {
        String scope = URLEncoder.encode(
                "https://www.googleapis.com/auth/calendar https://www.googleapis.com/auth/userinfo.email",
                StandardCharsets.UTF_8
        );

        return props.getAuthUri()
                + "?client_id=" + props.getClientId()
                + "&redirect_uri=" + props.getRedirectUri()
                + "&response_type=code"
                + "&scope=" + scope
                + "&access_type=offline"
                + "&prompt=consent";
    }

    public String exchangeCodeForToken(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("client_id", props.getClientId());
        formData.add("client_secret", props.getClientSecret());
        formData.add("redirect_uri", props.getRedirectUri());
        formData.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(props.getTokenUrl(), request, String.class);
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        // Optional: Save refresh_token from first time login
        if (jsonNode.has("refresh_token")) {
            String refreshToken = jsonNode.get("refresh_token").asText();
            saveRefreshToken(refreshToken); // Implement this to save in DB
        }

        JsonNode json = new ObjectMapper().readTree(response.getBody());

        String accessToken = json.get("access_token").asText();

        // ✅ In refresh_token để lấy và dùng vĩnh viễn
        if (json.has("refresh_token")) {
            String refreshToken = json.get("refresh_token").asText();
            System.out.println("👉 REFRESH TOKEN: " + refreshToken);
            // TODO: Lưu refreshToken vào file/database
        }

        return jsonNode.get("access_token").asText();
    }

    public String createGoogleMeet(String accessToken) throws Exception {
        HttpRequestInitializer requestInitializer = request -> {
            request.getHeaders().setAuthorization("Bearer " + accessToken);
        };

        Calendar calendarService = new Calendar.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                requestInitializer
        ).setApplicationName("SMK Meet App").build();

        Event event = new Event()
                .setSummary("Cuộc họp SMK")
                .setDescription("Tạo cuộc họp tự động bằng Google Meet");

        DateTime startDateTime = new DateTime(System.currentTimeMillis() + 3600000);
        DateTime endDateTime = new DateTime(System.currentTimeMillis() + 7200000);

        EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("Asia/Ho_Chi_Minh");
        EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("Asia/Ho_Chi_Minh");

        event.setStart(start);
        event.setEnd(end);

        ConferenceData conferenceData = new ConferenceData();
        CreateConferenceRequest conferenceRequest = new CreateConferenceRequest()
                .setRequestId(UUID.randomUUID().toString())
                .setConferenceSolutionKey(new ConferenceSolutionKey().setType("hangoutsMeet"));

        conferenceData.setCreateRequest(conferenceRequest);
        event.setConferenceData(conferenceData);

        Event createdEvent = calendarService.events()
                .insert("primary", event)
                .setConferenceDataVersion(1)
                .execute();

        return createdEvent.getHangoutLink();
    }

    public String createGoogleMeetLink(String summary, Date startTime, Date endTime) throws Exception {
        String accessToken = getSystemAccessToken();

        Calendar service = new Calendar.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                request -> request.getHeaders().setAuthorization("Bearer " + accessToken)
        ).setApplicationName("SMK Meet App").build();

        Event event = new Event().setSummary(summary);

        EventDateTime start = new EventDateTime()
                .setDateTime(new DateTime(startTime))
                .setTimeZone("Asia/Ho_Chi_Minh");
        EventDateTime end = new EventDateTime()
                .setDateTime(new DateTime(endTime))
                .setTimeZone("Asia/Ho_Chi_Minh");

        event.setStart(start);
        event.setEnd(end);

        ConferenceData conferenceData = new ConferenceData();
        conferenceData.setCreateRequest(new CreateConferenceRequest()
                .setRequestId(UUID.randomUUID().toString())
                .setConferenceSolutionKey(new ConferenceSolutionKey().setType("hangoutsMeet")));
        event.setConferenceData(conferenceData);

        Event createdEvent = service.events()
                .insert("primary", event)
                .setConferenceDataVersion(1)
                .execute();

        return createdEvent.getHangoutLink();
    }

    public String refreshAccessToken(String refreshToken) throws Exception {
        RestTemplate rest = new RestTemplate();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", props.getClientId());
        form.add("client_secret", props.getClientSecret());
        form.add("refresh_token", refreshToken);
        form.add("grant_type", "refresh_token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<?> request = new HttpEntity<>(form, headers);

        ResponseEntity<String> response = rest.postForEntity(props.getTokenUrl(), request, String.class);
        JsonNode json = new ObjectMapper().readTree(response.getBody());

        return json.get("access_token").asText();
    }

    // Use this in production: read from DB or secured place
    public String getSystemAccessToken() throws Exception {
        return refreshAccessToken(refreshToken);
    }

    // Stub for saving refresh token securely (you should implement DB logic)
    private void saveRefreshToken(String refreshToken) {
        // TODO: Save this securely in DB for later use
        System.out.println("[DEBUG] Saved refresh_token: " + refreshToken);
    }
}
