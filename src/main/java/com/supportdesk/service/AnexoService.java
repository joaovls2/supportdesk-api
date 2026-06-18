package com.supportdesk.service;

import com.supportdesk.dto.AnexoResponseDTO;
import com.supportdesk.entity.Anexo;
import com.supportdesk.entity.Chamado;
import com.supportdesk.exception.BusinessException;
import com.supportdesk.exception.ResourceNotFoundException;
import com.supportdesk.repository.AnexoRepository;
import com.supportdesk.repository.ChamadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AnexoService {

    private final AnexoRepository anexoRepository;
    private final ChamadoRepository chamadoRepository;

    private final Path pastaUploads = Paths.get("uploads/anexos");

    public AnexoService(
            AnexoRepository anexoRepository,
            ChamadoRepository chamadoRepository) {

        this.anexoRepository = anexoRepository;
        this.chamadoRepository = chamadoRepository;
    }

    public List<AnexoResponseDTO> salvarAnexos(
            Long chamadoId,
            List<MultipartFile> arquivos) {

        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Chamado não encontrado"));

        if (arquivos == null || arquivos.isEmpty()) {
            throw new BusinessException("Nenhum arquivo enviado");
        }

        return arquivos.stream()
                .map(arquivo -> salvarArquivo(arquivo, chamado))
                .toList();
    }

    private AnexoResponseDTO salvarArquivo(
            MultipartFile arquivo,
            Chamado chamado) {

        try {
            validarArquivo(arquivo);

            Files.createDirectories(pastaUploads);

            String nomeOriginal = arquivo.getOriginalFilename();
            String extensao = obterExtensao(nomeOriginal);
            String nomeFinal = UUID.randomUUID() + extensao;

            Path caminhoFinal = pastaUploads.resolve(nomeFinal);

            Files.copy(
                    arquivo.getInputStream(),
                    caminhoFinal,
                    StandardCopyOption.REPLACE_EXISTING
            );

            Anexo anexo = new Anexo();
            anexo.setNomeArquivo(nomeOriginal);
            anexo.setTipoArquivo(arquivo.getContentType());
            anexo.setCaminhoArquivo(caminhoFinal.toString());
            anexo.setTamanhoArquivo(arquivo.getSize());
            anexo.setDataUpload(LocalDateTime.now());
            anexo.setChamado(chamado);

            Anexo anexoSalvo = anexoRepository.save(anexo);

            return converterParaResponseDTO(anexoSalvo);

        } catch (IOException e) {
            throw new BusinessException("Erro ao salvar arquivo");
        }
    }

    public List<AnexoResponseDTO> listarPorChamado(Long chamadoId) {

        if (!chamadoRepository.existsById(chamadoId)) {
            throw new ResourceNotFoundException("Chamado não encontrado");
        }

        return anexoRepository.findByChamadoId(chamadoId)
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    private void validarArquivo(MultipartFile arquivo) {

        if (arquivo.isEmpty()) {
            throw new BusinessException("Arquivo vazio");
        }

        String tipo = arquivo.getContentType();

        if (tipo == null ||
                !List.of("image/jpeg", "image/png", "image/webp")
                        .contains(tipo)) {

            throw new BusinessException(
                    "Tipo de arquivo inválido. Envie apenas JPG, PNG ou WEBP"
            );
        }
    }

    private String obterExtensao(String nomeArquivo) {

        if (nomeArquivo == null || !nomeArquivo.contains(".")) {
            return "";
        }

        return nomeArquivo.substring(nomeArquivo.lastIndexOf("."));
    }

    private AnexoResponseDTO converterParaResponseDTO(Anexo anexo) {

        return new AnexoResponseDTO(
                anexo.getId(),
                anexo.getNomeArquivo(),
                anexo.getTipoArquivo(),
                anexo.getCaminhoArquivo(),
                anexo.getTamanhoArquivo(),
                anexo.getDataUpload(),
                anexo.getChamado().getId()
        );
    }

    public Anexo buscarEntidadePorId(Long id) {

        return anexoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Anexo não encontrado"
                        ));
    }
}