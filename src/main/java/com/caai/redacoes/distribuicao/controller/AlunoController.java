package com.caai.redacoes.distribuicao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.caai.redacoes.distribuicao.service.AlunoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping("/importar")
    public ResponseEntity<String> importarCorretores(@RequestParam("file") MultipartFile file) {
        try {
            alunoService.processarPlanilha(file);
            return ResponseEntity.ok("Alunos importados com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao importar alunos: " + e.getMessage());
        }
    }
}
