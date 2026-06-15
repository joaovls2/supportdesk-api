package com.supportdesk.controller;

import com.supportdesk.dto.AdministradorResponseDTO;
import com.supportdesk.dto.AtualizarAdministradorDTO;
import com.supportdesk.dto.CriarAdministradorDTO;
import com.supportdesk.service.AdministradorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(
            AdministradorService administradorService) {

        this.administradorService = administradorService;
    }

    @PostMapping
    public ResponseEntity<AdministradorResponseDTO> salvar(
            @RequestBody CriarAdministradorDTO dto) {

        AdministradorResponseDTO administrador =
                administradorService.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(administrador);
    }

    @GetMapping
    public ResponseEntity<List<AdministradorResponseDTO>> listarTodos() {

        return ResponseEntity.ok(
                administradorService.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministradorResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                administradorService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministradorResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AtualizarAdministradorDTO dto) {

        return ResponseEntity.ok(
                administradorService.atualizar(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        administradorService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}