package com.caai.redacoes.distribuicao.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caai.redacoes.distribuicao.service.DistribuicaoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/distribuicao")
@RequiredArgsConstructor
class DistribuicaoController {
    private final DistribuicaoService service;

    @PostMapping("/gerar")
    public String gerarDistribuicao() {
        service.distribuirAlunosParaCorretores();
        return "Distribuição gerada para a semana ";
    }
}
