package com.supportdesk.entity;

import com.supportdesk.entity.enums.StatusChamado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "historicos_chamados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoChamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusChamado status;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comentario;

    @Column(nullable = false)
    private LocalDateTime dataAlteracao;

    @ManyToOne
    @JoinColumn(name = "chamado_id", nullable = false)
    private Chamado chamado;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;
}