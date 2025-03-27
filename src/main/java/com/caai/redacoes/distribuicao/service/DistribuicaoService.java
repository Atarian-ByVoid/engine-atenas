package com.caai.redacoes.distribuicao.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.caai.redacoes.distribuicao.model.Aluno;
import com.caai.redacoes.distribuicao.model.Corretor;
import com.caai.redacoes.distribuicao.model.Distribuicao;
import com.caai.redacoes.distribuicao.repository.AlunoRepository;
import com.caai.redacoes.distribuicao.repository.CorretorRepository;
import com.caai.redacoes.distribuicao.repository.DistribuicaoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DistribuicaoService {

    private final CorretorRepository corretorRepository;
    private final AlunoRepository alunoRepository;
    private final DistribuicaoRepository distribuicaoRepository;

    public void distribuirAlunosParaCorretores() {
        // Busca todos os corretores e alunos
        List<Corretor> corretores = corretorRepository.findAll();
        List<Aluno> alunos = alunoRepository.findAll();

        // Busca a última semana já processada, se não houver, começa na semana 1
        Integer ultimaSemana = distribuicaoRepository.findUltimaSemana();
        int semana = (ultimaSemana != null) ? ultimaSemana + 1 : 1;

        // Cria uma lista para armazenar a distribuição
        List<Distribuicao> distribuicoes = new ArrayList<>();

        // Variável para controlar a distribuição dos alunos para os corretores
        int alunoIndex = 0;
        
        // Número de corretores
        int totalCorretores = corretores.size();
        
        // Para cada aluno, atribuímos a um corretor
        for (Aluno aluno : alunos) {
            // Verifica os corretores já atribuídos a esse aluno nas semanas anteriores
            List<Corretor> corretoresHistorico = distribuicaoRepository.findCorretoresPorAluno(aluno.getId());

            // Variável para armazenar o corretor que será atribuído ao aluno
            Corretor corretorSelecionado = null;

            // Loop até encontrar um corretor que não tenha corrigido esse aluno antes
            for (int i = 0; i < totalCorretores; i++) {
                Corretor corretor = corretores.get((alunoIndex + i) % totalCorretores);
                if (!corretoresHistorico.contains(corretor)) {
                    corretorSelecionado = corretor;
                    break;
                }
            }

            // Se não houver corretor disponível (o que seria estranho), podemos lançar uma exceção ou tratar o caso de forma especial
            if (corretorSelecionado == null) {
                throw new IllegalStateException("Não foi possível atribuir um corretor para o aluno " + aluno.getNome());
            }

            // Cria a distribuição de alunos para o corretor selecionado
            Distribuicao distribuicao = new Distribuicao();
            distribuicao.setAluno(aluno);
            distribuicao.setCorretor(corretorSelecionado);
            distribuicao.setSemana(semana);

            distribuicoes.add(distribuicao);

            // Avança para o próximo aluno
            alunoIndex++;
        }

        // Salva as distribuições no banco
        distribuicaoRepository.saveAll(distribuicoes);
    }
}
