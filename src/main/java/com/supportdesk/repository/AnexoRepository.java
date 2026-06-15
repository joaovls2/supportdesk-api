package com.supportdesk.repository;

import com.supportdesk.entity.Anexo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnexoRepository
        extends JpaRepository<Anexo, Long> {

    List<Anexo> findByChamadoId(Long chamadoId);

}