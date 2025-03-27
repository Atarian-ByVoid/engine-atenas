package com.caai.redacoes.distribuicao.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.caai.redacoes.distribuicao.model.Corretor;
import com.caai.redacoes.distribuicao.repository.CorretorRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CorretorService {

    @Autowired
    private CorretorRepository corretorRepository;

    public void processarPlanilha(MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        List<Corretor> corretores = new ArrayList<>();
        Set<String> existingCorretores = new HashSet<>();
        
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;

            String nome = row.getCell(0).getStringCellValue();
            String email = row.getCell(1).getStringCellValue();
            String telefone = row.getCell(2).getStringCellValue();
            
            String key = nome + email + telefone;
            if (!existingCorretores.contains(key)) {
                Corretor corretor = new Corretor();
                corretor.setNome(nome);
                corretor.setEmail(email);
                corretor.setTelefone(telefone);

                corretores.add(corretor);
                existingCorretores.add(key);
            }
        }

        // Salva os corretores únicos
        if (!corretores.isEmpty()) {
            corretorRepository.saveAll(corretores);
        }
    }
}
