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

    public DashboardResponseDTO obterDashboard(
            Long empresaId) {

        return new DashboardResponseDTO(
                chamadoRepository.countByEmpresaId(empresaId),
                chamadoRepository.countByEmpresaIdAndStatus(
                        empresaId,
                        StatusChamado.ABERTO
                ),
                chamadoRepository.countByEmpresaIdAndStatus(
                        empresaId,
                        StatusChamado.EM_ANDAMENTO
                ),
                chamadoRepository.countByEmpresaIdAndStatus(
                        empresaId,
                        StatusChamado.RESOLVIDO
                ),
                chamadoRepository.countByEmpresaIdAndStatus(
                        empresaId,
                        StatusChamado.FECHADO
                ),
                chamadoRepository.countByEmpresaIdAndStatus(
                        empresaId,
                        StatusChamado.REABERTO
                ),
                chamadoRepository.countByEmpresaIdAndStatus(
                        empresaId,
                        StatusChamado.CANCELADO
                )
        );
    }
}