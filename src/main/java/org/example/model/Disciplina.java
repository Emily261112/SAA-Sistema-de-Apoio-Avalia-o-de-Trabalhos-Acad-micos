package org.example.model;

public class Disciplina {

    private int idDisciplina;
    private String nome;

    // Precisamos de um objeto Professor para a chave estrangeira
    private Professor professor;

    // Construtor padrão (vazio)
    public Disciplina() {
    }

    // ✅ NOVO CONSTRUTOR ADICIONADO: Usado para criar o objeto stub com o ID
    public Disciplina(int idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    // --- Getters e Setters ---

    public int getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(int idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}