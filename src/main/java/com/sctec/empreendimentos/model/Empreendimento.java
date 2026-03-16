package com.sctec.empreendimentos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "empreendimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empreendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "nome_empreendedor", nullable = false)
    private String nomeEmpreendedor;

    @Column(nullable = false)
    private String municipio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Segmento segmento;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}
