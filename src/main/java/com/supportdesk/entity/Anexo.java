package com.supportdesk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "anexos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Anexo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeArquivo;

    @Column(nullable = false)
    private String tipoArquivo;

    @Column(nullable = false)
    private String caminhoArquivo;

    @Column(nullable = false)
    private Long tamanhoArquivo;

    @Column(nullable = false)
    private LocalDateTime dataUpload;

    @ManyToOne
    @JoinColumn(name = "chamado_id", nullable = false)
    private Chamado chamado;
}