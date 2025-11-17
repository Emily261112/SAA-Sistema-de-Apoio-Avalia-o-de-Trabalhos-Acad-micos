package org.example.model;

import java.time.LocalDate;

public class Avaliacao {

    private int idAvaliacao;
    private String codAvaliacao; // Ex: "AP1"
    private LocalDate dataPublicacao;
    private LocalDate prazo;

    // Chaves Estrangeiras como Objetos
    private Disciplina disciplina;
    private Professor professorCriador;

    public Avaliacao() {
    }

    // --- Getters e Setters ---
    // (Alt + Insert para gerar)

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
}