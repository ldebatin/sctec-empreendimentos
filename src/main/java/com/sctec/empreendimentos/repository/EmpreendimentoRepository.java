package com.sctec.empreendimentos.repository;

import com.sctec.empreendimentos.model.Empreendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpreendimentoRepository extends JpaRepository<Empreendimento, Integer> {
}
