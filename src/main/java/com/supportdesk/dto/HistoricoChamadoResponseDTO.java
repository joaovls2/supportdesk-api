package com.supportdesk.dto;

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
public class HistoricoChamadoResponseDTO {

    private Long id;

    private StatusChamado status;

    private String comentario;

    private LocalDateTime dataAlteracao;

    private Long chamadoId;

    private Long tecnicoId;
}