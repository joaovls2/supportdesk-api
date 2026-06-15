package com.supportdesk.dto;

import com.supportdesk.entity.enums.CategoriaChamado;
import com.supportdesk.entity.enums.PrioridadeChamado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarChamadoDTO {

    private String titulo;
    private String descricao;
    private CategoriaChamado categoria;
    private PrioridadeChamado prioridade;

}