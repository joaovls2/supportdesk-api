package com.supportdesk.service;

import com.supportdesk.dto.AtualizarUsuarioDTO;
import com.supportdesk.dto.CriarUsuarioDTO;
import com.supportdesk.dto.UsuarioResponseDTO;
import com.supportdesk.entity.Usuario;
import com.supportdesk.exception.BusinessException;
import com.supportdesk.exception.ResourceNotFoundException;
import com.supportdesk.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UsuarioResponseDTO converterParaResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }

    public UsuarioResponseDTO salvar(CriarUsuarioDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return converterParaResponseDTO(usuarioSalvo);
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado"));

        return converterParaResponseDTO(usuario);
    }

    public UsuarioResponseDTO atualizar(Long id, AtualizarUsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado"));

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        return converterParaResponseDTO(usuarioAtualizado);
    }

    public void deletar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado"));

        usuarioRepository.delete(usuario);
    }
}