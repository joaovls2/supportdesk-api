package com.supportdesk.repository;

import com.supportdesk.entity.Chamado;
import com.supportdesk.entity.enums.StatusChamado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChamadoRepository
        extends JpaRepository<Chamado, Long> {

    List<Chamado> findByUsuarioId(Long usuarioId);

    List<Chamado> findByTecnicoId(Long tecnicoId);

    List<Chamado> findByStatus(StatusChamado status);

    long countByStatus(StatusChamado status);
}