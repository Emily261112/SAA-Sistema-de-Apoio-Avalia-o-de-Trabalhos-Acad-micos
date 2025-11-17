package org.example.model;

import java.math.BigDecimal; // Importe o BigDecimal para 'peso'

public class Criterio {

    private int idCriterio;
    private String nome;
    private String descricao;
    private BigDecimal peso; // Usamos BigDecimal para dinheiro/pesos (DECIMAL)

    // Chave Estrangeira
    private Avaliacao avaliacao;

    public Criterio() {
    }

    // --- Getters e Setters ---
    // (Alt + Insert para gerar)

    public int getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }
}