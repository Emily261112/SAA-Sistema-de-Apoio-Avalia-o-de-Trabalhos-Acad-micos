package org.example.model;

public class Questao {
    private int idQuestao;
    private String enunciado;
    private String tipo;

    // --- NOVO CAMPO ---
    // Agora a questão é vinculada à Disciplina (Banco de Questões), não só a uma prova.
    private Disciplina disciplina;

    public Questao() {}

    public Questao(int id, String enunciado) {
        this.idQuestao = id;
        this.enunciado = enunciado;
    }

    // Getters e Setters originais
    public int getIdQuestao() { return idQuestao; }
    public void setIdQuestao(int idQuestao) { this.idQuestao = idQuestao; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    // --- NOVOS GETTERS E SETTERS ---
    public Disciplina getDisciplina() { return disciplina; }
    public void setDisciplina(Disciplina disciplina) { this.disciplina = disciplina; }
}