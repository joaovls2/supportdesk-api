package com.supportdesk.service;

import com.supportdesk.dto.CadastrarEmpresaDTO;
import com.supportdesk.dto.EmpresaCadastroResponseDTO;
import com.supportdesk.entity.Administrador;
import com.supportdesk.repository.AdministradorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import com.supportdesk.dto.AtualizarEmpresaDTO;
import com.supportdesk.dto.CriarEmpresaDTO;
import com.supportdesk.dto.EmpresaResponseDTO;
import com.supportdesk.entity.Empresa;
import com.supportdesk.exception.BusinessException;
import com.supportdesk.exception.ResourceNotFoundException;
import com.supportdesk.repository.EmpresaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    public EmpresaService(EmpresaRepository empresaRepository, AdministradorRepository administradorRepository, PasswordEncoder passwordEncoder) {
        this.empresaRepository = empresaRepository;
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private EmpresaResponseDTO converterParaResponseDTO(Empresa empresa) {
        return new EmpresaResponseDTO(
                empresa.getId(),
                empresa.getNome(),
                empresa.getCnpj()
        );
    }

    public EmpresaResponseDTO salvar(CriarEmpresaDTO dto) {

        if (empresaRepository.existsByCnpj(dto.getCnpj())) {
            throw new BusinessException("CNPJ já cadastrado");
        }

        Empresa empresa = new Empresa();
        empresa.setNome(dto.getNome());
        empresa.setCnpj(dto.getCnpj());

        Empresa empresaSalva = empresaRepository.save(empresa);

        return converterParaResponseDTO(empresaSalva);
    }

    public List<EmpresaResponseDTO> listarTodos() {
        return empresaRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public EmpresaResponseDTO buscarPorId(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Empresa não encontrada"));

        return converterParaResponseDTO(empresa);
    }

    public EmpresaResponseDTO atualizar(Long id, AtualizarEmpresaDTO dto) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Empresa não encontrada"));

        empresa.setNome(dto.getNome());
        empresa.setCnpj(dto.getCnpj());

        Empresa empresaAtualizada = empresaRepository.save(empresa);

        return converterParaResponseDTO(empresaAtualizada);
    }

    public void deletar(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Empresa não encontrada"));

        empresaRepository.delete(empresa);
    }

    @Transactional
    public EmpresaCadastroResponseDTO cadastrarEmpresa(
            CadastrarEmpresaDTO dto) {

        if (empresaRepository.existsByCnpj(dto.getCnpj())) {
            throw new BusinessException(
                    "CNPJ já cadastrado"
            );
        }

        if (administradorRepository.existsByEmail(
                dto.getEmailAdmin())) {

            throw new BusinessException(
                    "E-mail do administrador já cadastrado"
            );
        }

        Empresa empresa = new Empresa();
        empresa.setNome(dto.getNomeEmpresa());
        empresa.setCnpj(dto.getCnpj());

        Empresa empresaSalva =
                empresaRepository.save(empresa);

        Administrador administrador =
                new Administrador();

        administrador.setNome(dto.getNomeAdmin());
        administrador.setEmail(dto.getEmailAdmin());
        administrador.setSenha(
                passwordEncoder.encode(
                        dto.getSenhaAdmin()
                )
        );
        administrador.setEmpresa(empresaSalva);

        Administrador adminSalvo =
                administradorRepository.save(
                        administrador
                );

        return new EmpresaCadastroResponseDTO(
                empresaSalva.getId(),
                empresaSalva.getNome(),
                empresaSalva.getCnpj(),
                adminSalvo.getId(),
                adminSalvo.getNome(),
                adminSalvo.getEmail()
        );
    }
}