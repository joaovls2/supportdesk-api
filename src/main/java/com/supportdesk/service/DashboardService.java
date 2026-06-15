package com.supportdesk.service;

import com.supportdesk.dto.DashboardResponseDTO;
import com.supportdesk.entity.enums.StatusChamado;
import com.supportdesk.repository.ChamadoRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final ChamadoRepository chamadoRepository;

    public DashboardService(
            ChamadoRepository chamadoRepository) {

        this.chamadoRepository = chamadoRepository;
    }

    public DashboardResponseDTO obterDashboard() {

        return new DashboardResponseDTO(
                chamadoRepository.count(),
                chamadoRepository.countByStatus(StatusChamado.ABERTO),
                chamadoRepository.countByStatus(StatusChamado.EM_ANDAMENTO),
                chamadoRepository.countByStatus(StatusChamado.RESOLVIDO),
                chamadoRepository.countByStatus(StatusChamado.FECHADO),
                chamadoRepository.countByStatus(StatusChamado.REABERTO),
                chamadoRepository.countByStatus(StatusChamado.CANCELADO)
        );
    }
}