package org.example.model;

// Professor também herda de Usuario
public class Professor extends Usuario {

    // Construtor padrão
    public Professor() {
        super();
    }

    // ✅ NOVO CONSTRUTOR ADICIONADO: Usado para criar o objeto stub com o ID
    public Professor(int idUsuario) {
        super(); // Chama o construtor da classe pai (Usuario)
        this.setIdUsuario(idUsuario); // Seta o ID do Professor/Usuario
    }

    // Se professor tivesse campos extras (ex: "sala"), iriam aqui.
}