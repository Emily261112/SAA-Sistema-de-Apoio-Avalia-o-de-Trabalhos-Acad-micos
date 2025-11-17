package org.example.model;

import java.math.BigDecimal;

public class NotaCriterio {

    private int idNotaCriterio;
    private BigDecimal notaAtribuida;
    private String observacao;

    // Chaves Estrangeiras
    private Trabalho trabalho;
    private Criterio criterio;

    public NotaCriterio() {
    }

    // --- Getters e Setters ---
    // (Alt + Insert para gerar)

    public int getIdNotaCriterio() {
        return idNotaCriterio;
    }

    public void setIdNotaCriterio(int idNotaCriterio) {
        this.idNotaCriterio = idNotaCriterio;
    }

    public BigDecimal getNotaAtribuida() {
        return notaAtribuida;
    }

    public void setNotaAtribuida(BigDecimal notaAtribuida) {
        this.notaAtribuida = notaAtribuida;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Trabalho getTrabalho() {
        return trabalho;
    }

    public void setTrabalho(Trabalho trabalho) {
        this.trabalho = trabalho;
    }

    public Criterio getCriterio() {
        return criterio;
    }

    public void setCriterio(Criterio criterio) {
        this.criterio = criterio;
    }
}