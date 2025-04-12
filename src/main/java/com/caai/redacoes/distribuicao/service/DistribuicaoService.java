package com.caai.redacoes.distribuicao.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.caai.redacoes.distribuicao.model.Aluno;
import com.caai.redacoes.distribuicao.model.Corretor;
import com.caai.redacoes.distribuicao.model.Distribuicao;
import com.caai.redacoes.distribuicao.repository.AlunoRepository;
import com.caai.redacoes.distribuicao.repository.CorretorRepository;
import com.caai.redacoes.distribuicao.repository.DistribuicaoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DistribuicaoService {

    private final CorretorRepository corretorRepository;
    private final AlunoRepository alunoRepository;
    private final DistribuicaoRepository distribuicaoRepository;
    private final ExcelUtils excelUtils;
    private final ExportacaoService exportacaoService;

    public void distribuirAlunosParaCorretores() {
        List<Corretor> corretores = corretorRepository.findAll();
        List<Aluno> alunos = alunoRepository.findAll();
        Integer ultimaSemana = distribuicaoRepository.findUltimaSemana();
        int semana = (ultimaSemana != null) ? ultimaSemana + 1 : 1;
        List<Distribuicao> distribuicoes = new ArrayList<>();
        int alunoIndex = 0;
        int totalCorretores = corretores.size();
        for (Aluno aluno : alunos) {
            List<Corretor> corretoresHistorico = distribuicaoRepository.findCorretoresPorAluno(aluno.getId());
            Corretor corretorSelecionado = null;

            for (int i = 0; i < totalCorretores; i++) {
                Corretor corretor = corretores.get((alunoIndex + i) % totalCorretores);
                if (!corretoresHistorico.contains(corretor)) {
                    corretorSelecionado = corretor;
                    break;
                }
            }

            if (corretorSelecionado == null) {
                throw new IllegalStateException("Não foi possível atribuir um corretor para o aluno " + aluno.getNome());
            }

            Distribuicao distribuicao = new Distribuicao();
            distribuicao.setAluno(aluno);
            distribuicao.setCorretor(corretorSelecionado);
            distribuicao.setSemana(semana);

            distribuicoes.add(distribuicao);

            alunoIndex++;
        }

        distribuicaoRepository.saveAll(distribuicoes);
    }

    public byte[] processarDistribuicao(MultipartFile original, MultipartFile naoEntregaramFile) {
        try {
            Map<String, List<String>> distribuicaoOriginal = excelUtils.lerDistribuicaoOriginal(original.getInputStream());
            Set<String> alunosNaoEntregaram = excelUtils.lerAlunosQueNaoEntregaram(naoEntregaramFile.getInputStream());
    
            Map<String, List<String>> novaDistribuicao = balancearDistribuicaoFiltrada(distribuicaoOriginal, alunosNaoEntregaram);
    
            return excelUtils.gerarNovaPlanilha(novaDistribuicao);
    
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar as planilhas", e);
        }
    }
    

    private Map<String, List<String>> balancearDistribuicaoFiltrada(Map<String, List<String>> distribuicaoOriginal, Set<String> alunosNaoEntregaram) {
        Map<String, List<String>> novaDistribuicao = new LinkedHashMap<>();
        List<String> alunosFiltrados = new ArrayList<>();
    
        List<String> corretores = new ArrayList<>(distribuicaoOriginal.keySet());
    
        // 1. Coletar apenas os alunos que não entregaram, mantendo os nomes dos corretores
        for (Map.Entry<String, List<String>> entry : distribuicaoOriginal.entrySet()) {
            novaDistribuicao.put(entry.getKey(), new ArrayList<>()); // inicia com lista vazia
    
            for (String aluno : entry.getValue()) {
                if (alunosNaoEntregaram.contains(aluno)) {
                    alunosFiltrados.add(aluno); // coletamos os alunos a redistribuir
                }
            }
        }
    
        // 2. Redistribuir de forma balanceada
        int indexCorretor = 0;
        for (String aluno : alunosFiltrados) {
            String corretor = corretores.get(indexCorretor);
            novaDistribuicao.get(corretor).add(aluno);
            indexCorretor = (indexCorretor + 1) % corretores.size(); // round-robin
        }
    
        return novaDistribuicao;
    }
    
}
