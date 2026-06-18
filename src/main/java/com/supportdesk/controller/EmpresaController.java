package com.supportdesk.controller;

import com.supportdesk.dto.CadastrarEmpresaDTO;
import com.supportdesk.dto.EmpresaCadastroResponseDTO;
import com.supportdesk.dto.AtualizarEmpresaDTO;
import com.supportdesk.dto.CriarEmpresaDTO;
import com.supportdesk.dto.EmpresaResponseDTO;
import com.supportdesk.service.EmpresaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(
            EmpresaService empresaService) {

        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> salvar(
            @RequestBody CriarEmpresaDTO dto) {

        EmpresaResponseDTO empresa =
                empresaService.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresa);
    }

    @GetMapping
    public ResponseEntity<List<EmpresaResponseDTO>> listarTodos() {

        return ResponseEntity.ok(
                empresaService.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                empresaService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AtualizarEmpresaDTO dto) {

        return ResponseEntity.ok(
                empresaService.atualizar(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        empresaService.deletar(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<EmpresaCadastroResponseDTO> cadastrarEmpresa(
            @RequestBody CadastrarEmpresaDTO dto) {

        EmpresaCadastroResponseDTO cadastro =
                empresaService.cadastrarEmpresa(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cadastro);
    }
}