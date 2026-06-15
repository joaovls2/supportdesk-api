package com.supportdesk.service;

import com.supportdesk.dto.AtualizarTecnicoDTO;
import com.supportdesk.dto.CriarTecnicoDTO;
import com.supportdesk.dto.TecnicoResponseDTO;
import com.supportdesk.entity.Tecnico;
import com.supportdesk.exception.BusinessException;
import com.supportdesk.exception.ResourceNotFoundException;
import com.supportdesk.repository.TecnicoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;
    private final PasswordEncoder passwordEncoder;

    public TecnicoService(
            TecnicoRepository tecnicoRepository,
            PasswordEncoder passwordEncoder) {

        this.tecnicoRepository = tecnicoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private TecnicoResponseDTO converterParaResponseDTO(Tecnico tecnico) {
        return new TecnicoResponseDTO(
                tecnico.getId(),
                tecnico.getNome(),
                tecnico.getEmail()
        );
    }

    public TecnicoResponseDTO salvar(CriarTecnicoDTO dto) {

        if (tecnicoRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        Tecnico tecnico = new Tecnico();
        tecnico.setNome(dto.getNome());
        tecnico.setEmail(dto.getEmail());
        tecnico.setSenha(passwordEncoder.encode(dto.getSenha()));

        Tecnico tecnicoSalvo = tecnicoRepository.save(tecnico);

        return converterParaResponseDTO(tecnicoSalvo);
    }

    public List<TecnicoResponseDTO> listarTodos() {
        return tecnicoRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public TecnicoResponseDTO buscarPorId(Long id) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Técnico não encontrado"));

        return converterParaResponseDTO(tecnico);
    }

    public TecnicoResponseDTO atualizar(Long id, AtualizarTecnicoDTO dto) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Técnico não encontrado"));

        tecnico.setNome(dto.getNome());
        tecnico.setEmail(dto.getEmail());
        tecnico.setSenha(passwordEncoder.encode(dto.getSenha()));

        Tecnico tecnicoAtualizado = tecnicoRepository.save(tecnico);

        return converterParaResponseDTO(tecnicoAtualizado);
    }

    public void deletar(Long id) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Técnico não encontrado"));

        tecnicoRepository.delete(tecnico);
    }
}