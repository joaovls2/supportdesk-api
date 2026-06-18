package com.supportdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarEmpresaDTO {

    private String nomeEmpresa;
    private String cnpj;

    private String nomeAdmin;
    private String emailAdmin;
    private String senhaAdmin;
}