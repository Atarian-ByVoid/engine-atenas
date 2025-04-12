package com.caai.redacoes.distribuicao.controller;

import com.caai.redacoes.distribuicao.dto.CorretorDTO;
import com.caai.redacoes.distribuicao.service.CorretorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/corretores")
@RequiredArgsConstructor
public class CorretorController {

    private final CorretorService corretorService;

    @Operation(summary = "Adicionar um novo corretor", description = "Adiciona um corretor à base de dados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Corretor adicionado com sucesso", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Erro ao adicionar corretor")
    })
    @PostMapping
    public ResponseEntity<?> adicionarCorretor(@RequestBody CorretorDTO corretorDTO) {
        try {
            CorretorDTO novoCorretor = corretorService.adicionarCorretor(corretorDTO);
            return ResponseEntity.ok(novoCorretor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Listar todos os corretores", description = "Retorna uma lista de todos os corretores cadastrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de corretores", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<List<CorretorDTO>> listarCorretores() {
        return ResponseEntity.ok(corretorService.listarCorretores());
    }

    @Operation(summary = "Atualizar um corretor", description = "Atualiza as informações de um corretor existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Corretor atualizado com sucesso", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Erro ao atualizar corretor"),
        @ApiResponse(responseCode = "404", description = "Corretor não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCorretor(@PathVariable Long id, @RequestBody CorretorDTO corretorDTO) {
        try {
            CorretorDTO corretorAtualizado = corretorService.atualizarCorretor(id, corretorDTO);
            return ResponseEntity.ok(corretorAtualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Remover um corretor", description = "Remove um corretor da base de dados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Corretor removido com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao remover corretor"),
        @ApiResponse(responseCode = "404", description = "Corretor não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerCorretor(@RequestParam Long id) {
        try {
            corretorService.removerCorretor(id);
            return ResponseEntity.ok("Corretor removido com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Importar corretores", description = "Importa uma lista de corretores a partir de um arquivo Excel.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Corretores importados com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro ao importar corretores")
    })
    @PostMapping("/importar")
    public ResponseEntity<?> importarCorretores(@RequestParam("file") MultipartFile file) {
        try {
            corretorService.processarPlanilha(file);
            return ResponseEntity.ok("Corretores importados com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao importar corretores: " + e.getMessage());
        }
    }
}
