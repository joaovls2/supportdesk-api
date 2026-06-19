package com.supportdesk.controller;

import com.supportdesk.dto.IASolicitacaoDTO;
import com.supportdesk.dto.IASugestaoChamadoDTO;
import com.supportdesk.service.IAService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ia")
public class IAController {

    private final IAService iaService;

    public IAController(IAService iaService) {
        this.iaService = iaService;
    }

    @PostMapping("/sugerir-chamado")
    public ResponseEntity<IASugestaoChamadoDTO> sugerir(
            @RequestBody IASolicitacaoDTO dto
    ) throws Exception {

        return ResponseEntity.ok(
                iaService.sugerir(dto)
        );
    }
}