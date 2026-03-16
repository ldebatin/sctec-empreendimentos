package com.sctec.empreendimentos.service;

import com.sctec.empreendimentos.dto.EmpreendimentoRequest;
import com.sctec.empreendimentos.dto.EmpreendimentoResponse;
import com.sctec.empreendimentos.model.Empreendimento;
import com.sctec.empreendimentos.repository.EmpreendimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpreendimentoService {

    private final EmpreendimentoRepository repository;

    public List<EmpreendimentoResponse> findAll() {
        return repository.findAll().stream()
                .map(EmpreendimentoResponse::from)
                .toList();
    }

    public EmpreendimentoResponse findById(Integer id) {
        return repository.findById(id)
                .map(EmpreendimentoResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Empreendimento não encontrado com id: " + id));
    }

    public EmpreendimentoResponse create(EmpreendimentoRequest request) {
        Empreendimento empreendimento = Empreendimento.builder()
                .nome(request.nome())
                .nomeEmpreendedor(request.nomeEmpreendedor())
                .municipio(request.municipio())
                .segmento(request.segmento())
                .email(request.email())
                .telefone(request.telefone())
                .status(request.status())
                .build();
        return EmpreendimentoResponse.from(repository.save(empreendimento));
    }

    public EmpreendimentoResponse update(Integer id, EmpreendimentoRequest request) {
        Empreendimento empreendimento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empreendimento não encontrado com id: " + id));
        empreendimento.setNome(request.nome());
        empreendimento.setNomeEmpreendedor(request.nomeEmpreendedor());
        empreendimento.setMunicipio(request.municipio());
        empreendimento.setSegmento(request.segmento());
        empreendimento.setEmail(request.email());
        empreendimento.setTelefone(request.telefone());
        empreendimento.setStatus(request.status());
        return EmpreendimentoResponse.from(repository.save(empreendimento));
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Empreendimento não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
