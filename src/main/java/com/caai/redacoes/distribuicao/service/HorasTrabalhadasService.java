package com.caai.redacoes.distribuicao.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.caai.redacoes.distribuicao.model.Corretor;
import com.caai.redacoes.distribuicao.model.HorasTrabalhadas;
import com.caai.redacoes.distribuicao.repository.CorretorRepository;
import com.caai.redacoes.distribuicao.repository.HorasTrabalhadasRepository;

import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HorasTrabalhadasService {

    private final CorretorRepository corretorRepository;

    private final HorasTrabalhadasRepository horasTrabalhadasRepository;

    public void processarPlanilhaDeHoras(MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;

            String nomeCorretor = row.getCell(0).getStringCellValue(); 
            Integer horasCorrigidas = (int) row.getCell(1).getNumericCellValue(); 

            Optional<Corretor> corretor = corretorRepository.findByNome(nomeCorretor);

            if (corretor != null) {
                HorasTrabalhadas horasTrabalhadas = horasTrabalhadasRepository.findByCorretorNome(nomeCorretor);

                if (horasTrabalhadas == null) {
                    horasTrabalhadas = new HorasTrabalhadas();
                    horasTrabalhadas.setCorretor(corretor.get());
                    horasTrabalhadas.setHorasTrabalhadas(horasCorrigidas);
                } else {
                    horasTrabalhadas.setHorasTrabalhadas(horasCorrigidas);
                }

                horasTrabalhadasRepository.save(horasTrabalhadas);
            } else {
                System.out.println("Corretor com nome " + nomeCorretor + " não encontrado.");
            }
        }
    }
}
