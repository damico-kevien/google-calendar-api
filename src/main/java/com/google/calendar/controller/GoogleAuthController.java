package com.google.calendar.controller;

import com.google.calendar.service.GoogleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1")
public class GoogleAuthController {

    private final GoogleService service;

    @GetMapping("/oauth2/login")
    public RedirectView googleLogin() {
        return service.login();
    }

    @GetMapping("/oauth2/callback")
    public ResponseEntity<String> googleCallback(@RequestParam(name = "code") String code) {
        return service.callback(code);
    }

    @GetMapping("/oauth2/tokeninfo")
    public ResponseEntity<?> getTokenInfo(@RequestParam("access_token") String token) {
        return service.getTokenInfo(token);
    }

}
