package com.sctec.empreendimentos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sctec.empreendimentos.config.SecurityConfig;
import com.sctec.empreendimentos.dto.EmpreendimentoRequest;
import com.sctec.empreendimentos.dto.EmpreendimentoResponse;
import com.sctec.empreendimentos.model.Segmento;
import com.sctec.empreendimentos.model.Status;
import com.sctec.empreendimentos.service.EmpreendimentoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpreendimentoController.class)
@Import(SecurityConfig.class)
class EmpreendimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmpreendimentoService service;

    private EmpreendimentoResponse response;
    private EmpreendimentoRequest request;

    @BeforeEach
    void setUp() {
        response = new EmpreendimentoResponse(
                1,
                "TechCatarinense",
                "Ana Paula Silva",
                "Florianópolis",
                Segmento.TECNOLOGIA,
                "ana@techcatarinense.com.br",
                "(48) 99999-1001",
                Status.ATIVO
        );

        request = new EmpreendimentoRequest(
                "TechCatarinense",
                "Ana Paula Silva",
                "Florianópolis",
                Segmento.TECNOLOGIA,
                "ana@techcatarinense.com.br",
                "(48) 99999-1001",
                Status.ATIVO
        );
    }

    @Test
    void findAll_deveRetornar200ComListaDeEmpreendimentos() throws Exception {
        when(service.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/empreendimentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("TechCatarinense"))
                .andExpect(jsonPath("$[0].segmento").value("TECNOLOGIA"));
    }

    @Test
    void findAll_deveRetornar200ComListaVazia() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/empreendimentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void findById_deveRetornar200QuandoEncontrado() throws Exception {
        when(service.findById(1)).thenReturn(response);

        mockMvc.perform(get("/api/empreendimentos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("TechCatarinense"))
                .andExpect(jsonPath("$.municipio").value("Florianópolis"))
                .andExpect(jsonPath("$.status").value("ATIVO"));
    }

    @Test
    void findById_deveRetornar404QuandoNaoEncontrado() throws Exception {
        when(service.findById(99)).thenThrow(new EntityNotFoundException("Empreendimento não encontrado com id: 99"));

        mockMvc.perform(get("/api/empreendimentos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Empreendimento não encontrado com id: 99"));
    }

    @Test
    void create_deveRetornar201QuandoDadosValidos() throws Exception {
        when(service.create(any(EmpreendimentoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/empreendimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("TechCatarinense"));
    }

    @Test
    void create_deveRetornar400QuandoNomeFaltando() throws Exception {
        EmpreendimentoRequest invalido = new EmpreendimentoRequest(
                "",
                "Ana Paula Silva",
                "Florianópolis",
                Segmento.TECNOLOGIA,
                "ana@techcatarinense.com.br",
                "(48) 99999-1001",
                Status.ATIVO
        );

        mockMvc.perform(post("/api/empreendimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.nome").exists());
    }

    @Test
    void create_deveRetornar400QuandoEmailInvalido() throws Exception {
        EmpreendimentoRequest invalido = new EmpreendimentoRequest(
                "TechCatarinense",
                "Ana Paula Silva",
                "Florianópolis",
                Segmento.TECNOLOGIA,
                "email-invalido",
                "(48) 99999-1001",
                Status.ATIVO
        );

        mockMvc.perform(post("/api/empreendimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void update_deveRetornar200QuandoDadosValidos() throws Exception {
        EmpreendimentoResponse atualizado = new EmpreendimentoResponse(
                1, "TechCatarinense Atualizado", "Ana Paula Silva",
                "Florianópolis", Segmento.TECNOLOGIA,
                "ana@techcatarinense.com.br", "(48) 99999-1001", Status.INATIVO
        );

        when(service.update(eq(1), any(EmpreendimentoRequest.class))).thenReturn(atualizado);

        mockMvc.perform(put("/api/empreendimentos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("TechCatarinense Atualizado"))
                .andExpect(jsonPath("$.status").value("INATIVO"));
    }

    @Test
    void update_deveRetornar404QuandoNaoEncontrado() throws Exception {
        when(service.update(eq(99), any(EmpreendimentoRequest.class)))
                .thenThrow(new EntityNotFoundException("Empreendimento não encontrado com id: 99"));

        mockMvc.perform(put("/api/empreendimentos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_deveRetornar204QuandoEncontrado() throws Exception {
        doNothing().when(service).delete(1);

        mockMvc.perform(delete("/api/empreendimentos/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(1);
    }

    @Test
    void delete_deveRetornar404QuandoNaoEncontrado() throws Exception {
        doThrow(new EntityNotFoundException("Empreendimento não encontrado com id: 99"))
                .when(service).delete(99);

        mockMvc.perform(delete("/api/empreendimentos/99"))
                .andExpect(status().isNotFound());
    }
}
