package com.supportdesk.service;

import com.supportdesk.dto.AtualizarUsuarioDTO;
import com.supportdesk.dto.CriarUsuarioDTO;
import com.supportdesk.dto.UsuarioResponseDTO;
import com.supportdesk.entity.Empresa;
import com.supportdesk.entity.Usuario;
import com.supportdesk.exception.BusinessException;
import com.supportdesk.exception.ResourceNotFoundException;
import com.supportdesk.repository.EmpresaRepository;
import com.supportdesk.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            EmpresaRepository empresaRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UsuarioResponseDTO converterParaResponseDTO(
            Usuario usuario) {

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getEmpresa().getId()
        );
    }

    public UsuarioResponseDTO salvar(
            CriarUsuarioDTO dto,
            Long empresaId) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Empresa não encontrada"
                        ));

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setEmpresa(empresa);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return converterParaResponseDTO(usuarioSalvo);
    }

    public List<UsuarioResponseDTO> listarTodos(
            Long empresaId) {

        return usuarioRepository.findByEmpresaId(empresaId)
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(
            Long id,
            Long empresaId) {

        Usuario usuario = usuarioRepository
                .findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuário não encontrado"
                        ));

        return converterParaResponseDTO(usuario);
    }

    public UsuarioResponseDTO atualizar(
            Long id,
            AtualizarUsuarioDTO dto,
            Long empresaId) {

        Usuario usuario = usuarioRepository
                .findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuário não encontrado"
                        ));

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        Usuario usuarioAtualizado =
                usuarioRepository.save(usuario);

        return converterParaResponseDTO(usuarioAtualizado);
    }

    public void deletar(
            Long id,
            Long empresaId) {

        Usuario usuario = usuarioRepository
                .findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuário não encontrado"
                        ));

        usuarioRepository.delete(usuario);
    }
}