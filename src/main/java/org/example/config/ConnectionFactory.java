package org.example.config; // Garante que está no pacote certo

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    // 1. CONFIGURE COM SEUS DADOS DO POSTGRESQL
    // Lembre-se de criar esse banco "avaliador_db" no seu PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/avaliador_db";
    private static final String USER = "postgres"; // Seu usuário (geralmente postgres)
    private static final String PASSWORD = "aluno123"; // SUA SENHA DO BANCO

    public static Connection getConnection() {
        try {
            // 2. Carrega o driver (Graças ao pom.xml)
            Class.forName("org.postgresql.Driver");

            // 3. Retorna a conexão
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            // Lança uma exceção em tempo de execução
            throw new RuntimeException("Erro na conexão com o Banco de Dados", e);
        }
    }
}