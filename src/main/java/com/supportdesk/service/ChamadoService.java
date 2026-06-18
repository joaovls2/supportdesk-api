package com.supportdesk.service;

import com.supportdesk.dto.AtualizarStatusChamadoDTO;
import com.supportdesk.dto.AtribuirTecnicoDTO;
import com.supportdesk.dto.ChamadoResponseDTO;
import com.supportdesk.dto.CriarChamadoDTO;
import com.supportdesk.entity.Chamado;
import com.supportdesk.entity.HistoricoChamado;
import com.supportdesk.entity.Tecnico;
import com.supportdesk.entity.Usuario;
import com.supportdesk.entity.enums.StatusChamado;
import com.supportdesk.exception.BusinessException;
import com.supportdesk.exception.ResourceNotFoundException;
import com.supportdesk.repository.ChamadoRepository;
import com.supportdesk.repository.HistoricoChamadoRepository;
import com.supportdesk.repository.TecnicoRepository;
import com.supportdesk.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistoricoChamadoRepository historicoChamadoRepository;
    private final TecnicoRepository tecnicoRepository;

    public ChamadoService(
            ChamadoRepository chamadoRepository,
            UsuarioRepository usuarioRepository,
            HistoricoChamadoRepository historicoChamadoRepository,
            TecnicoRepository tecnicoRepository) {

        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.historicoChamadoRepository = historicoChamadoRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    public ChamadoResponseDTO criarChamado(CriarChamadoDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado"));

        LocalDateTime agora = LocalDateTime.now();

        Chamado chamado = new Chamado();
        chamado.setTitulo(dto.getTitulo());
        chamado.setDescricao(dto.getDescricao());
        chamado.setCategoria(dto.getCategoria());
        chamado.setPrioridade(dto.getPrioridade());
        chamado.setStatus(StatusChamado.ABERTO);
        chamado.setDataCriacao(agora);
        chamado.setDataLimiteSla(
                agora.plusHours(dto.getPrioridade().getHorasSla())
        );
        chamado.setUsuario(usuario);
        chamado.setTecnico(null);
        chamado.setEmpresa(usuario.getEmpresa());

        Chamado chamadoSalvo = chamadoRepository.save(chamado);

        HistoricoChamado historico = new HistoricoChamado();
        historico.setStatus(StatusChamado.ABERTO);
        historico.setComentario("Chamado criado pelo usuário.");
        historico.setDataAlteracao(agora);
        historico.setChamado(chamadoSalvo);
        historico.setTecnico(null);

        historicoChamadoRepository.save(historico);

        return converterParaResponseDTO(chamadoSalvo);
    }

    public List<ChamadoResponseDTO> listarTodos(
            Long empresaId) {

        return chamadoRepository.findByEmpresaId(empresaId)
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public List<ChamadoResponseDTO> listarPorUsuario(
            Long usuarioId,
            Long empresaId) {

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }

        return chamadoRepository.findByUsuarioIdAndEmpresaId(usuarioId, empresaId)
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public List<ChamadoResponseDTO> listarPorTecnico(
            Long tecnicoId,
            Long empresaId) {

        if (!tecnicoRepository.existsById(tecnicoId)) {
            throw new ResourceNotFoundException("Técnico não encontrado");
        }

        return chamadoRepository.findByTecnicoIdAndEmpresaId(tecnicoId, empresaId)
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public ChamadoResponseDTO atribuirTecnico(
            Long chamadoId,
            AtribuirTecnicoDTO dto) {

        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Chamado não encontrado"));

        Tecnico tecnico = tecnicoRepository.findById(dto.getTecnicoId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Técnico não encontrado"));

        if (!chamado.getEmpresa().getId().equals(tecnico.getEmpresa().getId())) {
            throw new BusinessException(
                    "Técnico não pertence à mesma empresa do chamado."
            );
        }

        chamado.setTecnico(tecnico);

        Chamado chamadoAtualizado = chamadoRepository.save(chamado);

        HistoricoChamado historico = new HistoricoChamado();
        historico.setStatus(chamadoAtualizado.getStatus());
        historico.setComentario("Técnico atribuído ao chamado: " + tecnico.getNome());
        historico.setDataAlteracao(LocalDateTime.now());
        historico.setChamado(chamadoAtualizado);
        historico.setTecnico(tecnico);

        historicoChamadoRepository.save(historico);

        return converterParaResponseDTO(chamadoAtualizado);
    }

    public ChamadoResponseDTO buscarPorId(
            Long id,
            Long empresaId) {

        Chamado chamado = chamadoRepository
                .findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chamado não encontrado"
                        ));

        return converterParaResponseDTO(chamado);
    }

    public ChamadoResponseDTO atualizarStatus(
            Long chamadoId,
            AtualizarStatusChamadoDTO dto) {

        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Chamado não encontrado"));

        Tecnico tecnico = tecnicoRepository.findById(dto.getTecnicoId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Técnico não encontrado"));

        if (!chamado.getEmpresa().getId().equals(tecnico.getEmpresa().getId())) {
            throw new BusinessException(
                    "Técnico não pertence à mesma empresa do chamado."
            );
        }

        validarTransicaoStatus(
                chamado.getStatus(),
                dto.getStatus()
        );

        chamado.setStatus(dto.getStatus());

        Chamado chamadoAtualizado =
                chamadoRepository.save(chamado);

        HistoricoChamado historico =
                new HistoricoChamado();

        historico.setStatus(dto.getStatus());
        historico.setComentario(dto.getComentario());
        historico.setDataAlteracao(LocalDateTime.now());
        historico.setChamado(chamadoAtualizado);
        historico.setTecnico(tecnico);

        historicoChamadoRepository.save(historico);

        return converterParaResponseDTO(
                chamadoAtualizado
        );
    }

    public ChamadoResponseDTO cancelarChamado(Long chamadoId) {

        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Chamado não encontrado"));

        validarTransicaoStatus(
                chamado.getStatus(),
                StatusChamado.CANCELADO
        );

        chamado.setStatus(StatusChamado.CANCELADO);

        Chamado chamadoAtualizado =
                chamadoRepository.save(chamado);

        HistoricoChamado historico =
                new HistoricoChamado();

        historico.setStatus(StatusChamado.CANCELADO);
        historico.setComentario("Chamado cancelado.");
        historico.setDataAlteracao(LocalDateTime.now());
        historico.setChamado(chamadoAtualizado);
        historico.setTecnico(chamadoAtualizado.getTecnico());

        historicoChamadoRepository.save(historico);

        return converterParaResponseDTO(
                chamadoAtualizado
        );
    }

    public ChamadoResponseDTO reabrirChamado(Long chamadoId) {

        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Chamado não encontrado"));

        validarTransicaoStatus(
                chamado.getStatus(),
                StatusChamado.REABERTO
        );

        chamado.setStatus(StatusChamado.REABERTO);

        Chamado chamadoAtualizado =
                chamadoRepository.save(chamado);

        HistoricoChamado historico =
                new HistoricoChamado();

        historico.setStatus(StatusChamado.REABERTO);
        historico.setComentario("Chamado reaberto.");
        historico.setDataAlteracao(LocalDateTime.now());
        historico.setChamado(chamadoAtualizado);
        historico.setTecnico(chamadoAtualizado.getTecnico());

        historicoChamadoRepository.save(historico);

        return converterParaResponseDTO(
                chamadoAtualizado
        );
    }

    public ChamadoResponseDTO fecharChamado(Long chamadoId) {

        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Chamado não encontrado"));

        validarTransicaoStatus(
                chamado.getStatus(),
                StatusChamado.FECHADO
        );

        chamado.setStatus(StatusChamado.FECHADO);

        Chamado chamadoAtualizado =
                chamadoRepository.save(chamado);

        HistoricoChamado historico =
                new HistoricoChamado();

        historico.setStatus(StatusChamado.FECHADO);
        historico.setComentario("Chamado fechado.");
        historico.setDataAlteracao(LocalDateTime.now());
        historico.setChamado(chamadoAtualizado);
        historico.setTecnico(chamadoAtualizado.getTecnico());

        historicoChamadoRepository.save(historico);

        return converterParaResponseDTO(
                chamadoAtualizado
        );
    }

    private void validarTransicaoStatus(
            StatusChamado statusAtual,
            StatusChamado novoStatus) {

        if (statusAtual == StatusChamado.CANCELADO) {
            throw new BusinessException(
                    "Chamado cancelado não pode ser alterado."
            );
        }

        if (statusAtual == StatusChamado.FECHADO) {
            throw new BusinessException(
                    "Chamado fechado não pode ser alterado."
            );
        }

        if (statusAtual == StatusChamado.RESOLVIDO
                && novoStatus != StatusChamado.FECHADO
                && novoStatus != StatusChamado.REABERTO) {

            throw new BusinessException(
                    "Chamado resolvido só pode ser fechado ou reaberto."
            );
        }

        if (novoStatus == StatusChamado.ABERTO) {
            throw new BusinessException(
                    "Não é permitido alterar um chamado para ABERTO."
            );
        }
    }

    private ChamadoResponseDTO converterParaResponseDTO(Chamado chamado) {

        Long tecnicoId = null;

        if (chamado.getTecnico() != null) {
            tecnicoId = chamado.getTecnico().getId();
        }

        return new ChamadoResponseDTO(
                chamado.getId(),
                chamado.getTitulo(),
                chamado.getDescricao(),
                chamado.getStatus(),
                chamado.getPrioridade(),
                chamado.getCategoria(),
                chamado.getDataCriacao(),
                chamado.getDataLimiteSla(),
                chamado.getUsuario().getId(),
                tecnicoId,
                chamado.getEmpresa().getId()
        );
    }
}