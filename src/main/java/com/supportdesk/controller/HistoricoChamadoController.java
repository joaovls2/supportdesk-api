package com.supportdesk.controller;

import com.supportdesk.dto.HistoricoChamadoResponseDTO;
import com.supportdesk.service.HistoricoChamadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamados")
public class HistoricoChamadoController {

    private final HistoricoChamadoService historicoChamadoService;

    public HistoricoChamadoController(
            HistoricoChamadoService historicoChamadoService) {

        this.historicoChamadoService = historicoChamadoService;
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<List<HistoricoChamadoResponseDTO>> listarPorChamado(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                historicoChamadoService.listarPorChamado(id)
        );
    }
}