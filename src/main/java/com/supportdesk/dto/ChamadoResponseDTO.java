package com.supportdesk.dto;

import com.supportdesk.entity.enums.CategoriaChamado;
import com.supportdesk.entity.enums.PrioridadeChamado;
import com.supportdesk.entity.enums.StatusChamado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChamadoResponseDTO {

    private Long id;
    private String titulo;
    private String descricao;

    private StatusChamado status;
    private PrioridadeChamado prioridade;
    private CategoriaChamado categoria;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataLimiteSla;

    private Long usuarioId;
    private Long tecnicoId;
}