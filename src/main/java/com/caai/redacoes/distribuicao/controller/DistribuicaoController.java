package com.caai.redacoes.distribuicao.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.caai.redacoes.distribuicao.service.DistribuicaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/distribuicao")
@RequiredArgsConstructor
public class DistribuicaoController {

    private final DistribuicaoService distribuicaoService;

    @Operation(summary = "Gerar distribuição de alunos para corretores", description = "Distribute alunos to corretores for the current week.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Distribuição gerada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro ao gerar a distribuição")
    })
    @PostMapping("/gerar")
    public String gerarDistribuicao() {
        distribuicaoService.distribuirAlunosParaCorretores();
        return "Distribuição gerada para a semana.";
    }

    @Operation(summary = "Balancear planilha de corretores e alunos",
               description = "Recebe a planilha original e a planilha dos alunos que entregaram, e retorna uma nova planilha balanceada.")
    @PostMapping(value = "/balancear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> balancearPlanilha(
            @Parameter(description = "Planilha original com a distribuição anterior", required = true)
            @RequestPart("original") MultipartFile original,

            @Parameter(description = "Planilha com os nomes dos alunos que entregaram", required = true)
            @RequestPart("entregaram") MultipartFile entregaram) {

        byte[] novaPlanilha = distribuicaoService.processarDistribuicao(original, entregaram);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=nova-distribuicao.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(novaPlanilha);
    }
}