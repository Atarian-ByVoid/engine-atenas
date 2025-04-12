package com.caai.redacoes.distribuicao.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.StringUtils;

import com.caai.redacoes.distribuicao.dto.CorretorDTO;
import com.caai.redacoes.distribuicao.mapper.CorretorMapper;
import com.caai.redacoes.distribuicao.model.Corretor;
import com.caai.redacoes.distribuicao.repository.CorretorRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CorretorService {

    @Autowired
    private CorretorRepository corretorRepository;

    @Autowired
    private CorretorMapper corretorMapper;

    public CorretorDTO adicionarCorretor(CorretorDTO dto) {
        Optional<Corretor> existente = corretorRepository.findByNome(dto.getNome());
        if (existente.isPresent()) {
            throw new RuntimeException("Já existe um corretor cadastrado com este e-mail!");
        }

        Corretor corretor = corretorMapper.dtoParaCorretor(dto);
        corretorRepository.save(corretor);
        return corretorMapper.corretorParaDTO(corretor);
    }

    public List<CorretorDTO> listarCorretores() {
        return corretorRepository.findAll()
                .stream()
                .map(corretorMapper::corretorParaDTO)
                .collect(Collectors.toList());
    }

    public CorretorDTO atualizarCorretor(Long id, CorretorDTO dto) {
        Corretor corretor = corretorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Corretor não encontrado"));

        corretor.setNome(dto.getNome());
        corretor.setEmail(dto.getEmail());
        corretor.setTelefone(dto.getTelefone());

        corretorRepository.save(corretor);
        return corretorMapper.corretorParaDTO(corretor);
    }

    public void removerCorretor(Long id) {
        if (!corretorRepository.existsById(id)) {
            throw new RuntimeException("Corretor não encontrado");
        }
        corretorRepository.deleteById(id);
    }

    public void processarPlanilha(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
    
            Sheet sheet = workbook.getSheetAt(0);
            List<Corretor> corretores = new ArrayList<>();
            Set<String> existingCorretores = new HashSet<>();
    
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
    
                Cell cell = row.getCell(0);
                if (cell == null || cell.getCellType() != CellType.STRING) continue;
    
                String nome = cell.getStringCellValue().trim();
                nome = nome.replaceAll("\\s+", " ");
    
                if (StringUtils.isNotBlank(nome) && existingCorretores.add(nome)) {
                    Corretor corretor = new Corretor();
                    corretor.setNome(nome);
                    corretores.add(corretor);
                }
            }
    
            if (!corretores.isEmpty()) {
                corretorRepository.saveAll(corretores);
            }
        }}

}
