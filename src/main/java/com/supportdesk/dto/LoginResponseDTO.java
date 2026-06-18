package com.supportdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String tipo;
    private Long empresaId;
    private String token;
}