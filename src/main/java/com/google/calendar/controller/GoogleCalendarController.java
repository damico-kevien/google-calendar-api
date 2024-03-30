package com.google.calendar.controller;

import com.google.calendar.model.dto.GoogleEventDto;
import com.google.calendar.service.GoogleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/calendars")
public class GoogleCalendarController {

    private final GoogleService service;

    @GetMapping
    public ResponseEntity<?> getCalendars(@RequestHeader(name = "Google") String token) {
        return service.getCalendars(token);
    }

    @GetMapping("/{calendarId}")
    public ResponseEntity<?> getCalendar(@PathVariable String calendarId, @RequestHeader(name = "Google") String token) {
        return service.getCalendar(calendarId, token);
    }

    @PostMapping
    public ResponseEntity<?> createCalendar(@RequestParam String summary, @RequestHeader(name = "Google") String token) {
        return service.createCalendar(token, summary);
    }

    @PostMapping("/{calendarId}/events")
    public ResponseEntity<?> createEvent(@PathVariable String calendarId, @RequestHeader(name = "Google") String token, @RequestBody GoogleEventDto event) {
        return service.createEvent(calendarId, token, event);
    }

    @GetMapping("/{calendarId}/events")
    public ResponseEntity<?> getEvents(@PathVariable String calendarId, @RequestHeader(name = "Google") String token) {
        return service.getEvents(calendarId, token);
    }

    @GetMapping("/{calendarId}/events/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable String calendarId, @PathVariable String eventId, @RequestHeader(name = "Google") String token) {
        return service.getEvent(calendarId, eventId, token);
    }

    @PutMapping("/{calendarId}/events/{eventId}")
    public ResponseEntity<?> updateEvent(@PathVariable String calendarId, @PathVariable String eventId, @RequestHeader(name = "Google") String token, @RequestBody GoogleEventDto event) {
        return service.updateEvent(calendarId, eventId, token, event);
    }

    @DeleteMapping("/{calendarId}/events/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable String calendarId, @PathVariable String eventId, @RequestHeader(name = "Google") String token) {
        return service.deleteEvent(calendarId, eventId, token);
    }
}
