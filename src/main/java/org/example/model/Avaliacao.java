package org.example.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Avaliacao {

    private int idAvaliacao;
    private String codAvaliacao; // Ex: "AP1"
    private LocalDate dataPublicacao;
    private LocalDate prazo;

    // Chaves Estrangeiras como Objetos
    private Disciplina disciplina;
    private Professor professorCriador;

    // --- NOVOS CAMPOS ADICIONADOS ---
    private int maxQuestoes; // Corrige o erro do Servlet

    // Lista para guardar as questões vinculadas a esta prova (útil para a tela de Montar Prova)
    private List<Questao> questoes = new ArrayList<>();

    public Avaliacao() {
    }

    // --- Getters e Setters ---

    public int getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(int idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public String getCodAvaliacao() {
        return codAvaliacao;
    }

    public void setCodAvaliacao(String codAvaliacao) {
        this.codAvaliacao = codAvaliacao;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public LocalDate getPrazo() {
        return prazo;
    }

    public void setPrazo(LocalDate prazo) {
        this.prazo = prazo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Professor getProfessorCriador() {
        return professorCriador;
    }

    public void setProfessorCriador(Professor professorCriador) {
        this.professorCriador = professorCriador;
    }

    // --- NOVOS GETTERS E SETTERS (PARA O CÓDIGO FUNCIONAR) ---

    public int getMaxQuestoes() {
        return maxQuestoes;
    }

    public void setMaxQuestoes(int maxQuestoes) {
        this.maxQuestoes = maxQuestoes;
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }
}