package com.supportdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaCadastroResponseDTO {

    private Long empresaId;
    private String nomeEmpresa;
    private String cnpj;

    private Long administradorId;
    private String nomeAdmin;
    private String emailAdmin;
}