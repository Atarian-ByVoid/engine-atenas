package com.caai.redacoes.distribuicao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CorretorDTO {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;
    
    private String email;
    private String telefone;
}
