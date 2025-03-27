package com.caai.redacoes.distribuicao.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caai.redacoes.distribuicao.model.Corretor;
import com.caai.redacoes.distribuicao.model.Aluno;
import com.caai.redacoes.distribuicao.model.Cronograma;
import com.caai.redacoes.distribuicao.repository.CorretorRepository;
import com.caai.redacoes.distribuicao.repository.AlunoRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CronogramaService {

    @Autowired
    private CorretorRepository corretorRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    public byte[] gerarCronograma() throws IOException {
        List<Corretor> corretores = corretorRepository.findAll();
        List<Aluno> alunos = alunoRepository.findAll();

        List<Cronograma> cronogramas = new ArrayList<>();

        int alunoIndex = 0;
        int numCorretores = corretores.size();
        for (Aluno aluno : alunos) {
            Corretor corretor = corretores.get(alunoIndex % numCorretores);

            Cronograma cronograma = new Cronograma();
            cronograma.setNomeAluno(aluno.getNome()); 
            cronograma.setNomeCorretor(corretor.getNome()); 

            cronogramas.add(cronograma);

            alunoIndex++;
        }

        return gerarPlanilhaExcel(cronogramas);
    }
    private byte[] gerarPlanilhaExcel(List<Cronograma> cronogramas) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cronograma");
    
        Map<String, List<String>> distribucao = new HashMap<>();
    
        for (Cronograma cronograma : cronogramas) {
            distribucao.computeIfAbsent(cronograma.getNomeCorretor(), k -> new ArrayList<>())
                       .add(cronograma.getNomeAluno());
        }
    
        int maxAlunos = distribucao.values().stream()
                                   .mapToInt(List::size)
                                   .max()
                                   .orElse(0);
    
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Corretor");
        
        for (int i = 1; i <= maxAlunos; i++) {
            headerRow.createCell(i).setCellValue("Aluno " + i);
        }
    
        int rowIndex = 1;
        for (Map.Entry<String, List<String>> entry : distribucao.entrySet()) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(entry.getKey()); 
            
            List<String> alunos = entry.getValue();
            for (int i = 0; i < alunos.size(); i++) {
                row.createCell(i + 1).setCellValue(alunos.get(i));  
            }
        }
    
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
    
        return baos.toByteArray();
    }
    
    
}
