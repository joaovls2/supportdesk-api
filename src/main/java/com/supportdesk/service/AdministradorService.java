package com.supportdesk.service;

import com.supportdesk.dto.AdministradorResponseDTO;
import com.supportdesk.dto.AtualizarAdministradorDTO;
import com.supportdesk.dto.CriarAdministradorDTO;
import com.supportdesk.entity.Administrador;
import com.supportdesk.entity.Empresa;
import com.supportdesk.exception.BusinessException;
import com.supportdesk.exception.ResourceNotFoundException;
import com.supportdesk.repository.AdministradorRepository;
import com.supportdesk.repository.EmpresaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;

    public AdministradorService(
            AdministradorRepository administradorRepository,
            EmpresaRepository empresaRepository,
            PasswordEncoder passwordEncoder) {

        this.administradorRepository = administradorRepository;
        this.empresaRepository = empresaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private AdministradorResponseDTO converterParaResponseDTO(
            Administrador administrador) {

        return new AdministradorResponseDTO(
                administrador.getId(),
                administrador.getNome(),
                administrador.getEmail(),
                administrador.getEmpresa().getId()
        );
    }

    public AdministradorResponseDTO salvar(
            CriarAdministradorDTO dto,
            Long empresaId) {

        if (administradorRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Empresa não encontrada"));

        Administrador administrador = new Administrador();
        administrador.setNome(dto.getNome());
        administrador.setEmail(dto.getEmail());
        administrador.setSenha(passwordEncoder.encode(dto.getSenha()));
        administrador.setEmpresa(empresa);

        Administrador administradorSalvo =
                administradorRepository.save(administrador);

        return converterParaResponseDTO(administradorSalvo);
    }

    public List<AdministradorResponseDTO> listarTodos(Long empresaId) {

        return administradorRepository.findByEmpresaId(empresaId)
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public AdministradorResponseDTO buscarPorId(
            Long id,
            Long empresaId) {

        Administrador administrador = administradorRepository
                .findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Administrador não encontrado"
                        ));

        return converterParaResponseDTO(administrador);
    }

    public AdministradorResponseDTO atualizar(
            Long id,
            AtualizarAdministradorDTO dto,
            Long empresaId) {

        Administrador administrador = administradorRepository
                .findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Administrador não encontrado"
                        ));

        administrador.setNome(dto.getNome());
        administrador.setEmail(dto.getEmail());
        administrador.setSenha(passwordEncoder.encode(dto.getSenha()));

        Administrador administradorAtualizado =
                administradorRepository.save(administrador);

        return converterParaResponseDTO(administradorAtualizado);
    }

    public void deletar(
            Long id,
            Long empresaId) {

        Administrador administrador = administradorRepository
                .findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Administrador não encontrado"
                        ));

        administradorRepository.delete(administrador);
    }
}