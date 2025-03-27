package com.caai.redacoes.distribuicao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caai.redacoes.distribuicao.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long>  {
    
}
