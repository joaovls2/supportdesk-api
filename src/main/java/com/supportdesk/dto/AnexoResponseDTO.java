package com.supportdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnexoResponseDTO {

    private Long id;

    private String nomeArquivo;

    private String tipoArquivo;

    private String caminhoArquivo;

    private Long tamanhoArquivo;

    private LocalDateTime dataUpload;

    private Long chamadoId;
}