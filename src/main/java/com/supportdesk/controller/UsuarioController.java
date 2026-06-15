package com.supportdesk.controller;

import com.supportdesk.dto.ChamadoResponseDTO;
import com.supportdesk.dto.AtualizarUsuarioDTO;
import com.supportdesk.dto.CriarUsuarioDTO;
import com.supportdesk.dto.UsuarioResponseDTO;
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

    public UsuarioController(UsuarioService usuarioService, ChamadoService chamadoService) {
        this.usuarioService = usuarioService;
        this.chamadoService = chamadoService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvar(
            @RequestBody CriarUsuarioDTO dto) {

        UsuarioResponseDTO usuario =
                usuarioService.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {

        return ResponseEntity.ok(
                usuarioService.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                usuarioService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AtualizarUsuarioDTO dto) {

        return ResponseEntity.ok(
                usuarioService.atualizar(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        usuarioService.deletar(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/chamados")
    public ResponseEntity<List<ChamadoResponseDTO>> listarChamadosDoUsuario(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                chamadoService.listarPorUsuario(id)
        );
    }
}