package com.example.BYTEBRIDGERS.controller;

import com.example.BYTEBRIDGERS.dto.DashboardResponse;
import com.example.BYTEBRIDGERS.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(
            @RequestParam Integer userId,
            Authentication authentication) {

        Integer authenticatedUserId =
                Integer.parseInt(authentication.getName());

        DashboardResponse response =
                dashboardService.getDashboard(
                        userId,
                        authenticatedUserId
                );

        return ResponseEntity.ok(response);
    }
}