package com.supportdesk.repository;

import com.supportdesk.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministradorRepository
        extends JpaRepository<Administrador, Long> {

    boolean existsByEmail(String email);

    Optional<Administrador> findByEmail(String email);
}