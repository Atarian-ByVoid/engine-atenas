package com.caai.redacoes.distribuicao.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.caai.redacoes.distribuicao.model.Aluno;
import com.caai.redacoes.distribuicao.repository.AlunoRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public void processarPlanilha(MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        List<Aluno> alunos = new ArrayList<>();
        Set<String> existingAlunos = new HashSet<>();
        
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;

            String nome = row.getCell(0).getStringCellValue();
            String email = row.getCell(1).getStringCellValue();
            String telefone = row.getCell(2).getStringCellValue();
            
            String key = nome + email + telefone;
            if (!existingAlunos.contains(key)) {
                Aluno aluno = new Aluno();
                aluno.setNome(nome);
                aluno.setEmail(email);
                aluno.setTelefone(telefone);

                alunos.add(aluno);
                existingAlunos.add(key);
            }
        }

        if (!alunos.isEmpty()) {
            alunoRepository.saveAll(alunos);
        }
    }
}
