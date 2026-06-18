package com.supportdesk.controller;

import com.supportdesk.dto.DashboardResponseDTO;
import com.supportdesk.security.JwtService;
import com.supportdesk.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final JwtService jwtService;

    public DashboardController(
            DashboardService dashboardService,
            JwtService jwtService) {

        this.dashboardService = dashboardService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> obterDashboard(
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");

        Long empresaId = jwtService.extrairEmpresaId(token);

        return ResponseEntity.ok(
                dashboardService.obterDashboard(empresaId)
        );
    }
}