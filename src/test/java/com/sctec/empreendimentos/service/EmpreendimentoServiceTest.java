package com.sctec.empreendimentos.service;

import com.sctec.empreendimentos.dto.EmpreendimentoRequest;
import com.sctec.empreendimentos.dto.EmpreendimentoResponse;
import com.sctec.empreendimentos.model.Empreendimento;
import com.sctec.empreendimentos.model.Segmento;
import com.sctec.empreendimentos.model.Status;
import com.sctec.empreendimentos.repository.EmpreendimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpreendimentoServiceTest {

    @Mock
    private EmpreendimentoRepository repository;

    @InjectMocks
    private EmpreendimentoService service;

    private Empreendimento empreendimento;
    private EmpreendimentoRequest request;

    @BeforeEach
    void setUp() {
        empreendimento = Empreendimento.builder()
                .id(1)
                .nome("TechCatarinense")
                .nomeEmpreendedor("Ana Paula Silva")
                .municipio("Florianópolis")
                .segmento(Segmento.TECNOLOGIA)
                .email("ana@techcatarinense.com.br")
                .telefone("(48) 99999-1001")
                .status(Status.ATIVO)
                .build();

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
    void findAll_deveRetornarListaDeEmpreendimentos() {
        when(repository.findAll()).thenReturn(List.of(empreendimento));

        List<EmpreendimentoResponse> resultado = service.findAll();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).nome()).isEqualTo("TechCatarinense");
        assertThat(resultado.get(0).segmento()).isEqualTo(Segmento.TECNOLOGIA);
    }

    @Test
    void findAll_deveRetornarListaVaziaQuandoNaoHaRegistros() {
        when(repository.findAll()).thenReturn(List.of());

        List<EmpreendimentoResponse> resultado = service.findAll();

        assertThat(resultado).isEmpty();
    }

    @Test
    void findById_deveRetornarEmpreendimentoQuandoEncontrado() {
        when(repository.findById(1)).thenReturn(Optional.of(empreendimento));

        EmpreendimentoResponse resultado = service.findById(1);

        assertThat(resultado.id()).isEqualTo(1);
        assertThat(resultado.nome()).isEqualTo("TechCatarinense");
        assertThat(resultado.municipio()).isEqualTo("Florianópolis");
    }

    @Test
    void findById_deveLancarExcecaoQuandoNaoEncontrado() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_deveSalvarERetornarEmpreendimento() {
        when(repository.save(any(Empreendimento.class))).thenReturn(empreendimento);

        EmpreendimentoResponse resultado = service.create(request);

        assertThat(resultado.nome()).isEqualTo("TechCatarinense");
        assertThat(resultado.status()).isEqualTo(Status.ATIVO);
        verify(repository, times(1)).save(any(Empreendimento.class));
    }

    @Test
    void update_deveAtualizarERetornarEmpreendimento() {
        EmpreendimentoRequest requestAtualizado = new EmpreendimentoRequest(
                "TechCatarinense Atualizado",
                "Ana Paula Silva",
                "Florianópolis",
                Segmento.TECNOLOGIA,
                "ana@techcatarinense.com.br",
                "(48) 99999-1001",
                Status.INATIVO
        );

        Empreendimento atualizado = Empreendimento.builder()
                .id(1)
                .nome("TechCatarinense Atualizado")
                .nomeEmpreendedor("Ana Paula Silva")
                .municipio("Florianópolis")
                .segmento(Segmento.TECNOLOGIA)
                .email("ana@techcatarinense.com.br")
                .telefone("(48) 99999-1001")
                .status(Status.INATIVO)
                .build();

        when(repository.findById(1)).thenReturn(Optional.of(empreendimento));
        when(repository.save(any(Empreendimento.class))).thenReturn(atualizado);

        EmpreendimentoResponse resultado = service.update(1, requestAtualizado);

        assertThat(resultado.nome()).isEqualTo("TechCatarinense Atualizado");
        assertThat(resultado.status()).isEqualTo(Status.INATIVO);
    }

    @Test
    void update_deveLancarExcecaoQuandoNaoEncontrado() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(repository, never()).save(any());
    }

    @Test
    void delete_deveDeletarQuandoEncontrado() {
        when(repository.existsById(1)).thenReturn(true);

        service.delete(1);

        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void delete_deveLancarExcecaoQuandoNaoEncontrado() {
        when(repository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(repository, never()).deleteById(any());
    }
}
