package com.caai.redacoes.distribuicao.controller;

import com.caai.redacoes.distribuicao.dto.AlunoDTO;
import com.caai.redacoes.distribuicao.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @Operation(summary = "Adicionar um novo aluno", description = "Adiciona um aluno à base de dados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aluno adicionado com sucesso", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Erro ao adicionar aluno")
    })
    @PostMapping
    public ResponseEntity<?> adicionarAluno(@RequestBody AlunoDTO alunoDTO) {
        try {
            AlunoDTO novoAluno = alunoService.adicionarAluno(alunoDTO);
            return ResponseEntity.ok(novoAluno);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Listar todos os alunos", description = "Retorna uma lista de todos os alunos cadastrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de alunos", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<List<AlunoDTO>> listarAlunos() {
        return ResponseEntity.ok(alunoService.listarAlunos());
    }

    @Operation(summary = "Atualizar um aluno", description = "Atualiza as informações de um aluno existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Erro ao atualizar aluno"),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAluno(@PathVariable Long id, @RequestBody AlunoDTO alunoDTO) {
        try {
            AlunoDTO alunoAtualizado = alunoService.atualizarAluno(id, alunoDTO);
            return ResponseEntity.ok(alunoAtualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Remover um aluno", description = "Remove um aluno da base de dados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aluno removido com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao remover aluno"),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerAluno(@PathVariable Long id) {
        try {
            alunoService.removerAluno(id);
            return ResponseEntity.ok("Aluno removido com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Importar alunos", description = "Importa uma lista de alunos a partir de um arquivo Excel.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Alunos importados com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro ao importar alunos")
    })
    @PostMapping("/importar")
    public ResponseEntity<?> importarAlunos(@RequestParam("file") MultipartFile file) {
        try {
            alunoService.processarPlanilha(file);
            return ResponseEntity.ok("Alunos importados com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao importar alunos: " + e.getMessage());
        }
    }
}
