package com.supportdesk.service;

import com.supportdesk.dto.AdministradorResponseDTO;
import com.supportdesk.dto.AtualizarAdministradorDTO;
import com.supportdesk.dto.CriarAdministradorDTO;
import com.supportdesk.entity.Administrador;
import com.supportdesk.exception.BusinessException;
import com.supportdesk.exception.ResourceNotFoundException;
import com.supportdesk.repository.AdministradorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    public AdministradorService(
            AdministradorRepository administradorRepository,
            PasswordEncoder passwordEncoder) {

        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private AdministradorResponseDTO converterParaResponseDTO(
            Administrador administrador) {

        return new AdministradorResponseDTO(
                administrador.getId(),
                administrador.getNome(),
                administrador.getEmail()
        );
    }

    public AdministradorResponseDTO salvar(
            CriarAdministradorDTO dto) {

        if (administradorRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException(
                    "E-mail já cadastrado"
            );
        }

        Administrador administrador =
                new Administrador();

        administrador.setNome(dto.getNome());
        administrador.setEmail(dto.getEmail());
        administrador.setSenha(
                passwordEncoder.encode(dto.getSenha())
        );

        Administrador administradorSalvo =
                administradorRepository.save(administrador);

        return converterParaResponseDTO(
                administradorSalvo
        );
    }

    public List<AdministradorResponseDTO> listarTodos() {

        return administradorRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public AdministradorResponseDTO buscarPorId(
            Long id) {

        Administrador administrador =
                administradorRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Administrador não encontrado"
                                ));

        return converterParaResponseDTO(
                administrador
        );
    }

    public AdministradorResponseDTO atualizar(
            Long id,
            AtualizarAdministradorDTO dto) {

        Administrador administrador =
                administradorRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Administrador não encontrado"
                                ));

        administrador.setNome(dto.getNome());
        administrador.setEmail(dto.getEmail());
        administrador.setSenha(
                passwordEncoder.encode(dto.getSenha())
        );

        Administrador administradorAtualizado =
                administradorRepository.save(administrador);

        return converterParaResponseDTO(
                administradorAtualizado
        );
    }

    public void deletar(Long id) {

        Administrador administrador =
                administradorRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Administrador não encontrado"
                                ));

        administradorRepository.delete(administrador);
    }
}