package org.example.model;

import java.math.BigDecimal;

// Data Transfer Object para carregar o resultado do relatório de ranking
public class RankingDTO {

    private long rank; // A posição (1º, 2º, 3º)
    private String nomeEstudante;
    private BigDecimal notaFinal;
    private int idTrabalho;

    public RankingDTO() {
    }

    // --- Getters e Setters ---
    // (Alt + Insert para gerar)

    public long getRank() {
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }

    public String getNomeEstudante() {
        return nomeEstudante;
    }

    public void setNomeEstudante(String nomeEstudante) {
        this.nomeEstudante = nomeEstudante;
    }

    public BigDecimal getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(BigDecimal notaFinal) {
        this.notaFinal = notaFinal;
    }

    public int getIdTrabalho() {
        return idTrabalho;
    }

    public void setIdTrabalho(int idTrabalho) {
        this.idTrabalho = idTrabalho;
    }
}