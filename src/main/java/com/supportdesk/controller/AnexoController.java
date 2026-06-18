package com.supportdesk.controller;

import com.supportdesk.entity.Anexo;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.supportdesk.dto.AnexoResponseDTO;
import com.supportdesk.service.AnexoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/chamados")
public class AnexoController {

    private final AnexoService anexoService;

    public AnexoController(AnexoService anexoService) {
        this.anexoService = anexoService;
    }

    @PostMapping("/{id}/anexos")
    public ResponseEntity<List<AnexoResponseDTO>> enviarAnexos(
            @PathVariable Long id,
            @RequestParam("arquivos") List<MultipartFile> arquivos) {

        return ResponseEntity.ok(
                anexoService.salvarAnexos(id, arquivos)
        );
    }

    @GetMapping("/{id}/anexos")
    public ResponseEntity<List<AnexoResponseDTO>> listarAnexos(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                anexoService.listarPorChamado(id)
        );
    }

    @GetMapping("/anexos/{id}/download")
    public ResponseEntity<Resource> downloadAnexo(
            @PathVariable Long id) {

        Anexo anexo = anexoService.buscarEntidadePorId(id);

        Resource resource =
                new FileSystemResource(
                        anexo.getCaminhoArquivo()
                );

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(
                                anexo.getTipoArquivo()
                        )
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" +
                                anexo.getNomeArquivo() +
                                "\""
                )
                .body(resource);
    }
}