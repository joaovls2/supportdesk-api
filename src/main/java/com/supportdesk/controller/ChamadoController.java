package com.supportdesk.controller;

import com.supportdesk.dto.AtualizarStatusChamadoDTO;
import com.supportdesk.dto.AtribuirTecnicoDTO;
import com.supportdesk.dto.ChamadoResponseDTO;
import com.supportdesk.dto.CriarChamadoDTO;
import com.supportdesk.security.JwtService;
import com.supportdesk.service.ChamadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;
    private final JwtService jwtService;

    public ChamadoController(ChamadoService chamadoService, JwtService jwtService) {
        this.chamadoService = chamadoService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<ChamadoResponseDTO> criarChamado(
            @RequestBody CriarChamadoDTO dto) {

        ChamadoResponseDTO chamado =
                chamadoService.criarChamado(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(chamado);
    }

    @GetMapping
    public ResponseEntity<List<ChamadoResponseDTO>> listarTodos(
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        return ResponseEntity.ok(
                chamadoService.listarTodos(empresaId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChamadoResponseDTO> buscarPorId(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        return ResponseEntity.ok(
                chamadoService.buscarPorId(id, empresaId)
        );
    }

    private Long extrairEmpresaId(
            String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        return jwtService.extrairEmpresaId(token);
    }

    @PutMapping("/{id}/atribuir-tecnico")
    public ResponseEntity<ChamadoResponseDTO> atribuirTecnico(
            @PathVariable Long id,
            @RequestBody AtribuirTecnicoDTO dto) {

        return ResponseEntity.ok(
                chamadoService.atribuirTecnico(id, dto)
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ChamadoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusChamadoDTO dto) {

        return ResponseEntity.ok(
                chamadoService.atualizarStatus(id, dto)
        );
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ChamadoResponseDTO> cancelarChamado(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                chamadoService.cancelarChamado(id)
        );
    }

    @PutMapping("/{id}/reabrir")
    public ResponseEntity<ChamadoResponseDTO> reabrirChamado(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                chamadoService.reabrirChamado(id)
        );
    }

    @PutMapping("/{id}/fechar")
    public ResponseEntity<ChamadoResponseDTO> fecharChamado(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                chamadoService.fecharChamado(id)
        );
    }
}