package com.supportdesk.service;

import com.supportdesk.dto.HistoricoChamadoResponseDTO;
import com.supportdesk.entity.HistoricoChamado;
import com.supportdesk.exception.ResourceNotFoundException;
import com.supportdesk.repository.ChamadoRepository;
import com.supportdesk.repository.HistoricoChamadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricoChamadoService {

    private final HistoricoChamadoRepository historicoChamadoRepository;
    private final ChamadoRepository chamadoRepository;

    public HistoricoChamadoService(
            HistoricoChamadoRepository historicoChamadoRepository,
            ChamadoRepository chamadoRepository) {

        this.historicoChamadoRepository = historicoChamadoRepository;
        this.chamadoRepository = chamadoRepository;
    }

    public List<HistoricoChamadoResponseDTO> listarPorChamado(Long chamadoId) {

        if (!chamadoRepository.existsById(chamadoId)) {
            throw new ResourceNotFoundException("Chamado não encontrado");
        }

        return historicoChamadoRepository.findByChamadoId(chamadoId)
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    private HistoricoChamadoResponseDTO converterParaResponseDTO(
            HistoricoChamado historico) {

        Long tecnicoId = null;

        if (historico.getTecnico() != null) {
            tecnicoId = historico.getTecnico().getId();
        }

        return new HistoricoChamadoResponseDTO(
                historico.getId(),
                historico.getStatus(),
                historico.getComentario(),
                historico.getDataAlteracao(),
                historico.getChamado().getId(),
                tecnicoId
        );
    }
}