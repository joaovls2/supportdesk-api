package com.supportdesk.controller;

import com.supportdesk.dto.AtualizarTecnicoDTO;
import com.supportdesk.dto.ChamadoResponseDTO;
import com.supportdesk.dto.CriarTecnicoDTO;
import com.supportdesk.dto.TecnicoResponseDTO;
import com.supportdesk.security.JwtService;
import com.supportdesk.service.ChamadoService;
import com.supportdesk.service.TecnicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnicos")
public class TecnicoController {

    private final TecnicoService tecnicoService;
    private final ChamadoService chamadoService;
    private final JwtService jwtService;

    public TecnicoController(
            TecnicoService tecnicoService,
            ChamadoService chamadoService,
            JwtService jwtService) {

        this.tecnicoService = tecnicoService;
        this.chamadoService = chamadoService;
        this.jwtService = jwtService;
    }

    private Long extrairEmpresaId(
            String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        return jwtService.extrairEmpresaId(token);
    }

    @PostMapping
    public ResponseEntity<TecnicoResponseDTO> salvar(
            @RequestBody CriarTecnicoDTO dto,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        TecnicoResponseDTO tecnico =
                tecnicoService.salvar(dto, empresaId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tecnico);
    }

    @GetMapping
    public ResponseEntity<List<TecnicoResponseDTO>> listarTodos(
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        return ResponseEntity.ok(
                tecnicoService.listarTodos(empresaId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoResponseDTO> buscarPorId(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        return ResponseEntity.ok(
                tecnicoService.buscarPorId(id, empresaId)
        );
    }

    @GetMapping("/{id}/chamados")
    public ResponseEntity<List<ChamadoResponseDTO>> listarChamadosDoTecnico(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        return ResponseEntity.ok(
                chamadoService.listarPorTecnico(id, empresaId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TecnicoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AtualizarTecnicoDTO dto,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        return ResponseEntity.ok(
                tecnicoService.atualizar(id, dto, empresaId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        tecnicoService.deletar(id, empresaId);

        return ResponseEntity.noContent().build();
    }
}