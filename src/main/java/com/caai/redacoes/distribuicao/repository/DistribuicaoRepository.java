package com.caai.redacoes.distribuicao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.caai.redacoes.distribuicao.model.Corretor;
import com.caai.redacoes.distribuicao.model.Distribuicao;

public interface DistribuicaoRepository extends JpaRepository<Distribuicao, Long>  {
    List<Distribuicao> findBySemana(int semana);

    @Query("SELECT MAX(d.semana) FROM Distribuicao d")
    Integer findUltimaSemana();

    @Query("SELECT d.corretor FROM Distribuicao d WHERE d.aluno.id = :alunoId")
    List<Corretor> findCorretoresPorAluno(@Param("alunoId") Long alunoId);


}
