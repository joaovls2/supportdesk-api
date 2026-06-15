package com.supportdesk.dto;

import com.supportdesk.entity.enums.StatusChamado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarStatusChamadoDTO {

    private StatusChamado status;
    private String comentario;
    private Long tecnicoId;

}