package com.supportdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {

    private Long totalChamados;
    private Long chamadosAbertos;
    private Long chamadosEmAndamento;
    private Long chamadosResolvidos;
    private Long chamadosFechados;
    private Long chamadosReabertos;
    private Long chamadosCancelados;

}