package com.swd.smk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleMeetController {
    @Autowired
    private GoogleMeetService googleMeetService;

    // 1. Trả về URL để người dùng nhấn đăng nhập Google
    @GetMapping("/auth-url")
    public String getAuthUrl() {
        return googleMeetService.getAuthorizationUrl();
    }

    // 2. Google sẽ redirect về đây kèm code -> tạo Google Meet
    @GetMapping("/callback")
    public String handleGoogleCallback(@RequestParam("code") String code) {
        try {
            String accessToken = googleMeetService.exchangeCodeForToken(code);
            String meetUrl = googleMeetService.createGoogleMeet(accessToken);
            return "Google Meet Link: <a href=\"" + meetUrl + "\" target=\"_blank\">" + meetUrl + "</a>";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
