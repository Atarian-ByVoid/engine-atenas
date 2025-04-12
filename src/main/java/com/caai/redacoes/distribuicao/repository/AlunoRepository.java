package com.caai.redacoes.distribuicao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caai.redacoes.distribuicao.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long>  {

    boolean existsByNomeOrEmail(String nome, String email);

    boolean existsByNomeAndEmail(String nome, String email);

    boolean existsByNomeAndEmailAndIdNot(String nome, String email, Long id);

    Optional<Aluno> findByNome(String nome);
    
}
