package com.caai.redacoes.distribuicao.controller;

import com.caai.redacoes.distribuicao.service.CronogramaService;
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

    @GetMapping("/gerar")
    public ResponseEntity<byte[]> gerarCronograma() throws IOException {
        byte[] planilha = cronogramaService.gerarCronograma();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=cronograma.xlsx")
                .body(planilha);
    }
}
