package com.caai.redacoes.distribuicao.repository;

import com.caai.redacoes.distribuicao.model.HorasTrabalhadas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HorasTrabalhadasRepository extends JpaRepository<HorasTrabalhadas, Long> {

    HorasTrabalhadas findByCorretorNome(String nomeCorretor);
}

