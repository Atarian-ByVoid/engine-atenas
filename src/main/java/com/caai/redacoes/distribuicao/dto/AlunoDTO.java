package com.caai.redacoes.distribuicao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AlunoDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;
    
    private String email;
    private String telefone;
}
