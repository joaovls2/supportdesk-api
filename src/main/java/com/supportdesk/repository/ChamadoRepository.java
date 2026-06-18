package com.supportdesk.repository;

import com.supportdesk.entity.Chamado;
import com.supportdesk.entity.enums.StatusChamado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChamadoRepository
        extends JpaRepository<Chamado, Long> {

    List<Chamado> findByUsuarioId(Long usuarioId);

    List<Chamado> findByTecnicoId(Long tecnicoId);

    List<Chamado> findByStatus(StatusChamado status);

    long countByStatus(StatusChamado status);

    long countByEmpresaId(Long empresaId);

    long countByEmpresaIdAndStatus(
            Long empresaId,
            StatusChamado status
    );

    List<Chamado> findByEmpresaId(Long empresaId);

    Optional<Chamado> findByIdAndEmpresaId(
            Long id,
            Long empresaId
    );

    List<Chamado> findByUsuarioIdAndEmpresaId(
            Long usuarioId,
            Long empresaId
    );

    List<Chamado> findByTecnicoIdAndEmpresaId(
            Long tecnicoId,
            Long empresaId
    );
}