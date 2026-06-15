package com.supportdesk.controller;

import com.supportdesk.dto.LoginRequestDTO;
import com.supportdesk.dto.LoginResponseDTO;
import com.supportdesk.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/usuario")
    public ResponseEntity<LoginResponseDTO> loginUsuario(
            @RequestBody LoginRequestDTO dto) {

        return ResponseEntity.ok(
                authService.loginUsuario(dto)
        );
    }

    @PostMapping("/login/tecnico")
    public ResponseEntity<LoginResponseDTO> loginTecnico(
            @RequestBody LoginRequestDTO dto) {

        return ResponseEntity.ok(
                authService.loginTecnico(dto)
        );
    }

    @PostMapping("/login/admin")
    public ResponseEntity<LoginResponseDTO> loginAdministrador(
            @RequestBody LoginRequestDTO dto) {

        return ResponseEntity.ok(
                authService.loginAdministrador(dto)
        );
    }
}