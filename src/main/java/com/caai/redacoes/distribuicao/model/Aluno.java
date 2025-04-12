package com.caai.redacoes.distribuicao.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aluno extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    private String telefone;

    @Column(nullable = false)
    private String nome;

    public Aluno(String nome) {
        this.nome = nome;
    }
}
