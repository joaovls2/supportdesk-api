package com.supportdesk.controller;

import com.supportdesk.dto.AdministradorResponseDTO;
import com.supportdesk.dto.AtualizarAdministradorDTO;
import com.supportdesk.dto.CriarAdministradorDTO;
import com.supportdesk.security.JwtService;
import com.supportdesk.service.AdministradorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    private final AdministradorService administradorService;
    private final JwtService jwtService;

    public AdministradorController(
            AdministradorService administradorService,
            JwtService jwtService) {

        this.administradorService = administradorService;
        this.jwtService = jwtService;
    }

    private Long extrairEmpresaId(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return jwtService.extrairEmpresaId(token);
    }

    @PostMapping
    public ResponseEntity<AdministradorResponseDTO> salvar(
            @RequestBody CriarAdministradorDTO dto,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        AdministradorResponseDTO administrador =
                administradorService.salvar(dto, empresaId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(administrador);
    }

    @GetMapping
    public ResponseEntity<List<AdministradorResponseDTO>> listarTodos(
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        return ResponseEntity.ok(
                administradorService.listarTodos(empresaId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministradorResponseDTO> buscarPorId(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        return ResponseEntity.ok(
                administradorService.buscarPorId(id, empresaId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministradorResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AtualizarAdministradorDTO dto,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        return ResponseEntity.ok(
                administradorService.atualizar(id, dto, empresaId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        administradorService.deletar(id, empresaId);

        return ResponseEntity.noContent().build();
    }
}