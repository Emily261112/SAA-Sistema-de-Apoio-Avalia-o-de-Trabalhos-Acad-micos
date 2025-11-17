package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Estudante;
import org.example.model.Professor;
import org.example.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    /**
     * Este é o método mais complexo, pois salva em DUAS tabelas.
     */
    public void salvar(Usuario usuario) {
        String sqlUsuario = "INSERT INTO Usuario (pnome, snome, email, senha) VALUES (?, ?, ?, ?) RETURNING id_usuario";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario)) {

            conn.setAutoCommit(false);

            // === Parte 1: Inserir na tabela Usuario ===
            stmtUsuario.setString(1, usuario.getPnome());
            stmtUsuario.setString(2, usuario.getSnome());
            stmtUsuario.setString(3, usuario.getEmail());
            stmtUsuario.setString(4, usuario.getSenha());

            ResultSet rs = stmtUsuario.executeQuery();
            int novoIdUsuario = 0;
            if (rs.next()) {
                novoIdUsuario = rs.getInt(1);
                usuario.setIdUsuario(novoIdUsuario);
            }

            // === Parte 2: Inserir na tabela de Especialização ===
            if (usuario instanceof Estudante) {
                String sqlEstudante = "INSERT INTO Estudante (id_usuario) VALUES (?)";
                try (PreparedStatement stmtEstudante = conn.prepareStatement(sqlEstudante)) {
                    stmtEstudante.setInt(1, novoIdUsuario);
                    stmtEstudante.executeUpdate();
                }
            } else if (usuario instanceof Professor) {
                String sqlProfessor = "INSERT INTO Professor (id_usuario) VALUES (?)";
                try (PreparedStatement stmtProfessor = conn.prepareStatement(sqlProfessor)) {
                    stmtProfessor.setInt(1, novoIdUsuario);
                    stmtProfessor.executeUpdate();
                }
            }

            conn.commit();

        } catch (SQLException e) {
            System.err.println("Erro ao salvar usuário. Fazendo rollback.");
            e.printStackTrace();
        }
    }

    // ==================================================================
    // ▼▼▼ MÉTODO findById ATUALIZADO ▼▼▼
    // ==================================================================
    /**
     * Busca um usuário pelo ID e retorna o TIPO CORRETO
     * (Estudante ou Professor)
     */
    public Usuario findById(int id) {
        // Este SQL usa LEFT JOIN para checar as tabelas de especialização
        String sql = "SELECT u.*, " +
                "       CASE WHEN e.id_usuario IS NOT NULL THEN 'ESTUDANTE' " +
                "            WHEN p.id_usuario IS NOT NULL THEN 'PROFESSOR' " +
                "            ELSE 'USUARIO' END AS tipo " +
                "FROM Usuario u " +
                "LEFT JOIN Estudante e ON u.id_usuario = e.id_usuario " +
                "LEFT JOIN Professor p ON u.id_usuario = p.id_usuario " +
                "WHERE u.id_usuario = ?";

        Usuario usuario = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("tipo");

                    if ("ESTUDANTE".equals(tipo)) {
                        usuario = new Estudante();
                    } else if ("PROFESSOR".equals(tipo)) {
                        usuario = new Professor();
                    } else {
                        usuario = new Usuario();
                    }

                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setPnome(rs.getString("pnome"));
                    usuario.setSnome(rs.getString("snome"));
                    usuario.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    // ==================================================================
    // ▼▼▼ MÉTODO findAll ADICIONADO PARA O JSP ▼▼▼
    // ==================================================================
    /**
     * Busca todos os usuários (necessário para preencher menus de seleção no JSP).
     */
    public List<Usuario> findAll() {
        String sql = "SELECT * FROM Usuario";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setPnome(rs.getString("pnome"));
                usuario.setSnome(rs.getString("snome"));
                usuario.setEmail(rs.getString("email"));

                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os usuários: " + e.getMessage(), e);
        }
        return usuarios;
    }

    // ==================================================================
    // ▼▼▼ MÉTODO findByEmail ADICIONADO PARA O LOGIN ▼▼▼
    // ==================================================================
    /**
     * Busca um usuário pelo email (necessário para o login).
     * Usa LEFT JOIN para identificar o tipo (Professor/Estudante) e traz a senha.
     */
    public Usuario findByEmail(String email) {
        String sql = "SELECT u.*, " +
                "       CASE WHEN e.id_usuario IS NOT NULL THEN 'ESTUDANTE' " +
                "            WHEN p.id_usuario IS NOT NULL THEN 'PROFESSOR' " +
                "            ELSE 'USUARIO' END AS tipo " +
                "FROM Usuario u " +
                "LEFT JOIN Estudante e ON u.id_usuario = e.id_usuario " +
                "LEFT JOIN Professor p ON u.id_usuario = p.id_usuario " +
                "WHERE u.email = ?"; // Busca pelo email

        Usuario usuario = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("tipo");

                    // Decide qual classe instanciar (para a checagem no Servlet)
                    if ("PROFESSOR".equals(tipo)) {
                        usuario = new Professor();
                    } else if ("ESTUDANTE".equals(tipo)) {
                        usuario = new Estudante();
                    } else {
                        usuario = new Usuario();
                    }
                    // Mapeia os dados base, incluindo a senha para checagem
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setPnome(rs.getString("pnome"));
                    usuario.setSnome(rs.getString("snome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenha(rs.getString("senha")); // <-- TRAZENDO A SENHA DO BANCO
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por email: " + e.getMessage(), e);
        }
        return usuario;
    }
}