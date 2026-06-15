package com.supportdesk.controller;

import com.supportdesk.dto.DashboardResponseDTO;
import com.supportdesk.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponseDTO> obterDashboard() {

        return ResponseEntity.ok(
                dashboardService.obterDashboard()
        );
    }
}