package com.supportdesk.controller;

import com.supportdesk.dto.AtualizarUsuarioDTO;
import com.supportdesk.dto.ChamadoResponseDTO;
import com.supportdesk.dto.CriarUsuarioDTO;
import com.supportdesk.dto.UsuarioResponseDTO;
import com.supportdesk.security.JwtService;
import com.supportdesk.service.ChamadoService;
import com.supportdesk.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ChamadoService chamadoService;
    private final JwtService jwtService;

    public UsuarioController(
            UsuarioService usuarioService,
            ChamadoService chamadoService,
            JwtService jwtService) {

        this.usuarioService = usuarioService;
        this.chamadoService = chamadoService;
        this.jwtService = jwtService;
    }

    private Long extrairEmpresaId(
            String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        return jwtService.extrairEmpresaId(token);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvar(
            @RequestBody CriarUsuarioDTO dto,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        UsuarioResponseDTO usuario =
                usuarioService.salvar(dto, empresaId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos(
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        return ResponseEntity.ok(
                usuarioService.listarTodos(empresaId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        return ResponseEntity.ok(
                usuarioService.buscarPorId(id, empresaId)
        );
    }

    @GetMapping("/{id}/chamados")
    public ResponseEntity<List<ChamadoResponseDTO>> listarChamadosDoUsuario(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        return ResponseEntity.ok(
                chamadoService.listarPorUsuario(id, empresaId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AtualizarUsuarioDTO dto,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        return ResponseEntity.ok(
                usuarioService.atualizar(id, dto, empresaId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long empresaId = extrairEmpresaId(authorizationHeader);

        usuarioService.deletar(id, empresaId);

        return ResponseEntity.noContent().build();
    }
}