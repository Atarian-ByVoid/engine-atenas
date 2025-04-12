package com.caai.redacoes.distribuicao.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.caai.redacoes.distribuicao.service.HorasTrabalhadasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/horas")
@RequiredArgsConstructor
public class HorasTrabalhadasController {
    private final HorasTrabalhadasService service;

    @Operation(summary = "Importar planilha de horas trabalhadas", description = "Processa uma planilha Excel contendo os registros de horas trabalhadas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Planilha processada com sucesso",
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Erro ao importar a planilha",
                     content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/importar")
    public ResponseEntity<String> gerarCronograma(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            service.processarPlanilhaDeHoras(file);
            return ResponseEntity.ok("Planilha de horas importada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao importar planilha de horas: " + e.getMessage());
        }
    }
}
