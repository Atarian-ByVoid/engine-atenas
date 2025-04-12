package com.caai.redacoes.distribuicao.service;

import com.caai.redacoes.distribuicao.model.Distribuicao;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ExportacaoService {

    public byte[] gerarNovaPlanilha(Map<String, List<String>> distribuicao) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Nova Distribuição");

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Corretor");

            // Descobre o maior número de alunos
            int maxAlunos = distribuicao.values().stream()
                    .mapToInt(List::size)
                    .max()
                    .orElse(0);

            for (int i = 1; i <= maxAlunos; i++) {
                headerRow.createCell(i).setCellValue("Aluno " + i);
            }

            // Preenche os dados
            int rowIndex = 1;
            for (Map.Entry<String, List<String>> entry : distribuicao.entrySet()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(entry.getKey());

                List<String> alunos = entry.getValue();
                for (int i = 0; i < alunos.size(); i++) {
                    row.createCell(i + 1).setCellValue(alunos.get(i));
                }
            }

            // Escreve no ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar planilha", e);
        }
    }
}
