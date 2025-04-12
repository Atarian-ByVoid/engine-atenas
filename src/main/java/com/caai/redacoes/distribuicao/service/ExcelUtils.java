package com.caai.redacoes.distribuicao.service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class ExcelUtils {

    // Lê a planilha original de distribuição (corretor + lista de alunos)
    public Map<String, List<String>> lerDistribuicaoOriginal(InputStream inputStream) throws IOException {
        Map<String, List<String>> distribuicao = new LinkedHashMap<>();
    
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
    
            boolean isFirstRow = true;
            for (Row row : sheet) {
                if (isFirstRow) {
                    isFirstRow = false; // pula cabeçalho
                    continue;
                }
    
                Cell cellCorretor = row.getCell(0);
                if (cellCorretor == null || cellCorretor.getCellType() != CellType.STRING) continue;
    
                String nomeCorretor = cellCorretor.getStringCellValue().trim();
                List<String> alunos = new ArrayList<>();
    
                for (int i = 1; i < row.getLastCellNum(); i++) {
                    Cell cellAluno = row.getCell(i);
                    if (cellAluno != null && cellAluno.getCellType() == CellType.STRING) {
                        alunos.add(cellAluno.getStringCellValue().trim());
                    }
                }
    
                distribuicao.put(nomeCorretor, alunos);
            }
        }
    
        return distribuicao;
    }    

    // Lê a planilha com os alunos que NÃO entregaram a redação
    public Set<String> lerAlunosQueNaoEntregaram(InputStream inputStream) throws IOException {
        Set<String> alunos = new HashSet<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell cell = row.getCell(0);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    alunos.add(cell.getStringCellValue().trim());
                }
            }
        }

        return alunos;
    }

    // Gera a nova planilha balanceada
    public byte[] gerarNovaPlanilha(Map<String, List<String>> novaDistribuicao) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Nova Distribuição");

            int rowIndex = 0;
            for (Map.Entry<String, List<String>> entry : novaDistribuicao.entrySet()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(entry.getKey());

                List<String> alunos = entry.getValue();
                for (int i = 0; i < alunos.size(); i++) {
                    row.createCell(i + 1).setCellValue(alunos.get(i));
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }
}
