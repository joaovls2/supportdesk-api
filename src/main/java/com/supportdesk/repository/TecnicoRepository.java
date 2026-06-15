package com.supportdesk.repository;

import com.supportdesk.entity.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

    boolean existsByEmail(String email);

    Optional<Tecnico> findByEmail(String email);

}