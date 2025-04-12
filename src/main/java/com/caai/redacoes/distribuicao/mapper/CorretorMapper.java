package com.caai.redacoes.distribuicao.mapper;

import com.caai.redacoes.distribuicao.dto.CorretorDTO;
import com.caai.redacoes.distribuicao.model.Corretor;
import org.springframework.stereotype.Component;

@Component
public class CorretorMapper {

    public Corretor dtoParaCorretor(CorretorDTO dto) {
        return Corretor.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .build();
    }

    public CorretorDTO corretorParaDTO(Corretor corretor) {
        return CorretorDTO.builder()
                .nome(corretor.getNome())
                .email(corretor.getEmail())
                .telefone(corretor.getTelefone())
                .build();
    }
}
