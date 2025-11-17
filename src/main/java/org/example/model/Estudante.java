package org.example.model;

// "extends Usuario" significa que Estudante É UM Usuario
// e herda todos os campos: id, nome, email, etc.
public class Estudante extends Usuario {

    // Construtor
    public Estudante() {
        super(); // Chama o construtor do Usuario
    }

    // Por enquanto, a classe é idêntica ao Usuário.
    // Se no futuro um estudante tiver um campo extra (ex: "matricula"),
    // ele seria adicionado aqui.
}
