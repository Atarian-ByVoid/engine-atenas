package com.caai.redacoes.distribuicao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.caai.redacoes.distribuicao.dto.AlunoDTO;
import com.caai.redacoes.distribuicao.mapper.AlunoMapper;
import com.caai.redacoes.distribuicao.model.Aluno;
import com.caai.redacoes.distribuicao.repository.AlunoRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public AlunoDTO adicionarAluno(AlunoDTO dto) throws Exception {
        boolean alunoExiste = alunoRepository.existsByNomeAndEmail(dto.getNome(), dto.getEmail());

        if (alunoExiste) {
            throw new Exception("Aluno com nome '" + dto.getNome() + "' e email '" + dto.getEmail() + "' já existe.");
        }

        Aluno aluno = AlunoMapper.dtoParaAluno(dto);
        return AlunoMapper.alunoParaDTO(alunoRepository.save(aluno));
    }

    public List<AlunoDTO> listarAlunos() {
        return alunoRepository.findAll()
                .stream()
                .map(AlunoMapper::alunoParaDTO)
                .collect(Collectors.toList());
    }

    public AlunoDTO atualizarAluno(Long id, AlunoDTO novoAluno) throws Exception {
        Optional<Aluno> alunoExistente = alunoRepository.findById(id);
        if (alunoExistente.isPresent()) {
            boolean alunoDuplicado = alunoRepository.existsByNomeAndEmailAndIdNot(novoAluno.getNome(), novoAluno.getEmail(), id);
            if (alunoDuplicado) {
                throw new Exception("Já existe outro aluno com este nome e email.");
            }

            Aluno alunoAtualizado = alunoExistente.get();
            alunoAtualizado.setNome(novoAluno.getNome());
            alunoAtualizado.setEmail(novoAluno.getEmail());
            alunoAtualizado.setTelefone(novoAluno.getTelefone());

            return AlunoMapper.alunoParaDTO(alunoRepository.save(alunoAtualizado));
        } else {
            throw new Exception("Aluno não encontrado!");
        }
    }

    public void removerAluno(Long id) throws Exception {
        if (alunoRepository.existsById(id)) {
            alunoRepository.deleteById(id);
        } else {
            throw new Exception("Aluno não encontrado!");
        }
    }

    public void processarPlanilha(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
    
            Sheet sheet = workbook.getSheetAt(0);
            List<Aluno> alunosParaSalvar = new ArrayList<>();
    
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Ignorar cabeçalho
    
                Cell nomeCell = row.getCell(0);
                Cell statusCell = row.getCell(1);
    
                if (nomeCell == null || statusCell == null) continue;
    
                String nome = nomeCell.getStringCellValue().trim().replaceAll("\\s+", " ");
                String redacaoEntregue = "";
    
                // Verifica o tipo da célula antes de ler
                if (statusCell.getCellType() == CellType.BOOLEAN) {
                    redacaoEntregue = statusCell.getBooleanCellValue() ? "TRUE" : "FALSE";
                } else if (statusCell.getCellType() == CellType.STRING) {
                    redacaoEntregue = statusCell.getStringCellValue().trim().toUpperCase();
                }
    
                if (StringUtils.isNotBlank(nome) && "TRUE".equalsIgnoreCase(redacaoEntregue)) {
                    Aluno aluno = new Aluno();
                    aluno.setNome(nome);
                    alunosParaSalvar.add(aluno);
                }
            }
    
            if (!alunosParaSalvar.isEmpty()) {
                alunoRepository.saveAll(alunosParaSalvar);
                System.out.println(alunosParaSalvar.size() + " alunos salvos com sucesso.");
            }
        }
    }
}