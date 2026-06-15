package com.supportdesk.repository;

import com.supportdesk.entity.HistoricoChamado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoChamadoRepository
        extends JpaRepository<HistoricoChamado, Long> {

    List<HistoricoChamado> findByChamadoId(Long chamadoId);

}