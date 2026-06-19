package com.supportdesk.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supportdesk.dto.IASolicitacaoDTO;
import com.supportdesk.dto.IASugestaoChamadoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IAService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public IASugestaoChamadoDTO sugerir(IASolicitacaoDTO dto) throws Exception {

        String prompt = """
                Analise o problema informado.

                Retorne APENAS JSON.

                Categorias possíveis:
                SOFTWARE
                HARDWARE
                REDE
                ACESSO

                Prioridades possíveis:
                BAIXA
                MEDIA
                ALTA
                CRITICA

                Retorne exatamente:

                {
                  "titulo":"",
                  "descricao":"",
                  "categoria":"",
                  "prioridade":""
                }

                Problema:
                %s
                """.formatted(dto.getProblema());

        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
                        + apiKey;

        String body = """
                {
                  "contents":[
                    {
                      "parts":[
                        {
                          "text":"%s"
                        }
                      ]
                    }
                  ]
                }
                """.formatted(prompt.replace("\"", "\\\""));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity =
                new HttpEntity<>(body, headers);

        RestTemplate restTemplate =
                new RestTemplate();

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

        JsonNode root =
                objectMapper.readTree(response.getBody());

        String texto =
                root.path("candidates")
                        .get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text")
                        .asText();

        texto = texto
                .replace("```json", "")
                .replace("```", "")
                .trim();

        return objectMapper.readValue(
                texto,
                IASugestaoChamadoDTO.class
        );
    }
}