package com.caai.redacoes.distribuicao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caai.redacoes.distribuicao.model.Corretor;

public interface CorretorRepository extends JpaRepository<Corretor, Long> {

    Optional<Corretor> findByNome(String nomeCorretor);}
