package com.caai.redacoes.distribuicao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caai.redacoes.distribuicao.model.Corretor;

public interface CorretorRepository extends JpaRepository<Corretor, Long> {

    Corretor findByNome(String nomeCorretor);}
