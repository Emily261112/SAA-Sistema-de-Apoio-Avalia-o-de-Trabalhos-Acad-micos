package org.example.model;

public class Usuario {

    private int idUsuario; // Corresponde a id_usuario no banco
    private String pnome;
    private String snome;
    private String email;
    private String senha;

    // Construtor padrão (vazio) é importante
    public Usuario() {
    }

    // --- Getters e Setters ---
    // (O IntelliJ pode gerar isso para você: Alt + Insert -> Getters and Setters)

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPnome() {
        return pnome;
    }

    public void setPnome(String pnome) {
        this.pnome = pnome;
    }

    public String getSnome() {
        return snome;
    }

    public void setSnome(String snome) {
        this.snome = snome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}