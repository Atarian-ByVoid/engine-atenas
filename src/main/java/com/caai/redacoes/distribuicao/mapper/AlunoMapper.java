package com.caai.redacoes.distribuicao.mapper;

import com.caai.redacoes.distribuicao.dto.AlunoDTO;
import com.caai.redacoes.distribuicao.model.Aluno;

public class AlunoMapper {

    private AlunoMapper() {
    }

    public static Aluno dtoParaAluno(AlunoDTO dto) {
        return Aluno.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .build();
    }

    public static AlunoDTO alunoParaDTO(Aluno aluno) {
        return AlunoDTO.builder()
                .nome(aluno.getNome())
                .email(aluno.getEmail())
                .telefone(aluno.getTelefone())
                .build();
    }
}
