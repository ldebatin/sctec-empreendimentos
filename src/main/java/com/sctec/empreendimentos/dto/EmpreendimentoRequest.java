package com.sctec.empreendimentos.dto;

import com.sctec.empreendimentos.model.Segmento;
import com.sctec.empreendimentos.model.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmpreendimentoRequest(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Nome do(a) empreendedor(a) é obrigatório")
        String nomeEmpreendedor,

        @NotBlank(message = "Município é obrigatório")
        String municipio,

        @NotNull(message = "Segmento é obrigatório")
        Segmento segmento,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @NotNull(message = "Status é obrigatório")
        Status status
) {}
