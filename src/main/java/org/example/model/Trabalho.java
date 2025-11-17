package org.example.model;

import java.time.LocalDateTime; // Importe o LocalDateTime para 'data_envio'

public class Trabalho {

    private int idTrabalho;
    private String arquivoUrl; // O link para o arquivo (ex: "/uploads/trabalho1.pdf")
    private String status; // Ex: "Enviado", "Avaliado"
    private LocalDateTime dataEnvio; // Usamos LocalDateTime para TIMESTAMP

    // Chaves Estrangeiras
    private Avaliacao avaliacao;
    private Estudante estudante;

    public Trabalho() {
    }

    // --- Getters e Setters ---
    // (Alt + Insert para gerar)

    public int getIdTrabalho() {
        return idTrabalho;
    }

    public void setIdTrabalho(int idTrabalho) {
        this.idTrabalho = idTrabalho;
    }

    public String getArquivoUrl() {
        return arquivoUrl;
    }

    public void setArquivoUrl(String arquivoUrl) {
        this.arquivoUrl = arquivoUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }
}