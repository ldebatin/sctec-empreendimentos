package com.sctec.empreendimentos.dto;

import com.sctec.empreendimentos.model.Empreendimento;
import com.sctec.empreendimentos.model.Segmento;
import com.sctec.empreendimentos.model.Status;

public record EmpreendimentoResponse(
        Integer id,
        String nome,
        String nomeEmpreendedor,
        String municipio,
        Segmento segmento,
        String email,
        String telefone,
        Status status
) {
    public static EmpreendimentoResponse from(Empreendimento e) {
        return new EmpreendimentoResponse(
                e.getId(),
                e.getNome(),
                e.getNomeEmpreendedor(),
                e.getMunicipio(),
                e.getSegmento(),
                e.getEmail(),
                e.getTelefone(),
                e.getStatus()
        );
    }
}
