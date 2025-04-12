package com.caai.redacoes.distribuicao.controller;

import com.caai.redacoes.distribuicao.service.CronogramaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/cronograma")
public class CronogramaController {

    @Autowired
    private CronogramaService cronogramaService;

    @Operation(summary = "Gerar cronograma de distribuição", description = "Gera um cronograma em formato Excel para download.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cronograma gerado com sucesso", 
                     content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")),
        @ApiResponse(responseCode = "500", description = "Erro ao gerar cronograma")
    })
    @GetMapping("/gerar")
    public ResponseEntity<byte[]> gerarCronograma() throws IOException {
        byte[] planilha = cronogramaService.gerarCronograma();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=cronograma.xlsx")
                .body(planilha);
    }
}
