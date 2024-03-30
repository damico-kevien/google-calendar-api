package com.google.calendar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.calendar.model.Calendar;
import com.google.calendar.mapper.GoogleEventMapper;
import com.google.calendar.model.GoogleCredentials;
import com.google.calendar.model.dto.GoogleEventDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;
import java.util.StringJoiner;

@Service
public class GoogleService {

    @Value("${google.login.url}")
    private String googleLoginUrl;
    @Value("${application.base.url}")
    private String baseUrl;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private GoogleCredentials credentials;
    private final GoogleEventMapper mapper;

    public GoogleService(ResourceLoader resourceLoader, ObjectMapper objectMapper, GoogleEventMapper mapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
        this.mapper = mapper;
        this.restTemplate = new RestTemplate();
    }

    public RedirectView login() {
        this.credentials = getCredentials();
        StringJoiner joiner = new StringJoiner("&");
        joiner.add(googleLoginUrl + "?client_id=" + credentials.client_id());
        joiner.add("response_type=code");
        joiner.add("redirect_uri=" + this.baseUrl + "/oauth2/callback");
        joiner.add("scope=https://www.googleapis.com/auth/calendar+https://www.googleapis.com/auth/calendar.events");
        joiner.add("access_type=online");
        return new RedirectView(joiner.toString());
    }

    public ResponseEntity<String> callback(String code) {
        String url = "https://oauth2.googleapis.com/token";
        StringJoiner joiner = new StringJoiner("&");
        joiner.add(url + "?client_id=" + this.credentials.client_id());
        joiner.add("client_secret=" + this.credentials.client_secret());
        joiner.add("code=" + code);
        joiner.add("grant_type=authorization_code");
        joiner.add("redirect_uri=" + this.baseUrl + "/oauth2/callback");
        return restTemplate.postForEntity(joiner.toString(), null, String.class);
    }

    public ResponseEntity<?> getCalendars(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        String url = "https://www.googleapis.com/calendar/v3/users/me/calendarList/";
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<?> getCalendar(String calendarId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        String url = "https://www.googleapis.com/calendar/v3/calendars/" + calendarId;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<?> createCalendar(String token, String summary) {

        HttpHeaders headers = new HttpHeaders();
        Calendar calendar = new Calendar();
        calendar.setSummary(summary);
        calendar.setTimeZone("Europe/Rome");
        headers.add("Authorization", "Bearer " + token);
        String url = "https://www.googleapis.com/calendar/v3/calendars";
        HttpEntity<Calendar> entity = new HttpEntity<>(calendar, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    public ResponseEntity<?> createEvent(String calendarId, String token, GoogleEventDto event) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        String url = "https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events";
        HttpEntity<GoogleEventDto> entity = new HttpEntity<>(event, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    public ResponseEntity<?> getEvents(String calendarId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        String url = "https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events";
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<?> getEvent(String calendarId, String eventId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        String url = "https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events/" + eventId;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<?> updateEvent(String calendarId, String eventId, String token, GoogleEventDto event) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        String url = "https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events/" + eventId;
        GoogleEventDto eventUpdate = null;
        try {
            String eventString = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();
            eventUpdate = objectMapper.readValue(eventString, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        mapper.fromDto(event, eventUpdate);
        HttpEntity<GoogleEventDto> entity = new HttpEntity<>(eventUpdate, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    }

    public ResponseEntity<?> deleteEvent(String calendarId, String eventId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        String url = "https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events/" + eventId;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
    }

    private GoogleCredentials getCredentials() {
        GoogleCredentials credentials;
        try {
            Resource resource = resourceLoader.getResource("classpath:client_secret.json");
            File file = resource.getFile();
            credentials = objectMapper.readValue(file, new TypeReference<GoogleCredentials>() {
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("File client_secret.json not found");
        }
        return credentials;
    }

    public ResponseEntity<?> getTokenInfo(String token) {
        return restTemplate.getForEntity("https://oauth2.googleapis.com/tokeninfo?access_token=" + token, String.class);
    }
}
