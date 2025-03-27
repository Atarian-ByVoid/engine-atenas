package com.caai.redacoes.distribuicao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Distribuicao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Corretor corretor;
    
    @ManyToOne
    private Aluno aluno;
    
    private Integer semana;
}