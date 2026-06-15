package com.supportdesk.controller;

import com.supportdesk.dto.ChamadoResponseDTO;
import com.supportdesk.dto.AtualizarTecnicoDTO;
import com.supportdesk.dto.CriarTecnicoDTO;
import com.supportdesk.dto.TecnicoResponseDTO;
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

    public TecnicoController(TecnicoService tecnicoService, ChamadoService chamadoService) {
        this.tecnicoService = tecnicoService;
        this.chamadoService = chamadoService;
    }

    @PostMapping
    public ResponseEntity<TecnicoResponseDTO> salvar(
            @RequestBody CriarTecnicoDTO dto) {

        TecnicoResponseDTO tecnico =
                tecnicoService.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tecnico);
    }

    @GetMapping
    public ResponseEntity<List<TecnicoResponseDTO>> listarTodos() {

        return ResponseEntity.ok(
                tecnicoService.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                tecnicoService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TecnicoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AtualizarTecnicoDTO dto) {

        return ResponseEntity.ok(
                tecnicoService.atualizar(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        tecnicoService.deletar(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/chamados")
    public ResponseEntity<List<ChamadoResponseDTO>> listarChamadosDoTecnico(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                chamadoService.listarPorTecnico(id)
        );
    }
}