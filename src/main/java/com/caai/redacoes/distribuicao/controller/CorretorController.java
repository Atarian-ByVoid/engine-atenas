package com.caai.redacoes.distribuicao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.caai.redacoes.distribuicao.service.CorretorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/corretores")
@RequiredArgsConstructor
public class CorretorController {

    private final CorretorService corretorService;

    @PostMapping("/importar")
    public ResponseEntity<String> importarCorretores(@RequestParam("file") MultipartFile file) {
        try {
            corretorService.processarPlanilha(file);
            return ResponseEntity.ok("Corretores importados com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao importar corretores: " + e.getMessage());
        }
    }
}
