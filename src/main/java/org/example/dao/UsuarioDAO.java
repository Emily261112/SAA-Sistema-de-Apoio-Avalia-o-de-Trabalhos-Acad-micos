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
     * Salva um novo usuário (INSERT).
     * Gerencia a transação para salvar na tabela pai (Usuario) e filha (Estudante/Professor).
     */
    public void salvar(Usuario usuario) {
        String sqlUsuario = "INSERT INTO Usuario (pnome, snome, email, senha) VALUES (?, ?, ?, ?) RETURNING id_usuario";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario)) {

            conn.setAutoCommit(false); // Inicia transação

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

            conn.commit(); // Confirma transação

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Atualiza os dados de um usuário existente (UPDATE).
     */
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE Usuario SET pnome = ?, snome = ?, email = ?, senha = ? WHERE id_usuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getPnome());
            stmt.setString(2, usuario.getSnome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setInt(5, usuario.getIdUsuario());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    /**
     * Remove um usuário pelo ID (DELETE).
     */
    public void deletar(int id) {
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário: " + e.getMessage(), e);
        }
    }

    // ==================================================================
    // ▼▼▼ MÉTODO RENOMEADO: DE 'findById' PARA 'buscarPorId' ▼▼▼
    // ==================================================================
    public Usuario buscarPorId(int id) {
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
                    usuario.setSenha(rs.getString("senha"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    /**
     * Busca todos os usuários.
     */
    public List<Usuario> findAll() {
        String sql = "SELECT * FROM Usuario ORDER BY id_usuario";
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

    /**
     * Busca um usuário pelo email (Usado no Login).
     */
    public Usuario findByEmail(String email) {
        String sql = "SELECT u.*, " +
                "       CASE WHEN e.id_usuario IS NOT NULL THEN 'ESTUDANTE' " +
                "            WHEN p.id_usuario IS NOT NULL THEN 'PROFESSOR' " +
                "            ELSE 'USUARIO' END AS tipo " +
                "FROM Usuario u " +
                "LEFT JOIN Estudante e ON u.id_usuario = e.id_usuario " +
                "LEFT JOIN Professor p ON u.id_usuario = p.id_usuario " +
                "WHERE u.email = ?";

        Usuario usuario = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("tipo");

                    if ("PROFESSOR".equals(tipo)) {
                        usuario = new Professor();
                    } else if ("ESTUDANTE".equals(tipo)) {
                        usuario = new Estudante();
                    } else {
                        usuario = new Usuario();
                    }
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setPnome(rs.getString("pnome"));
                    usuario.setSnome(rs.getString("snome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenha(rs.getString("senha"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por email: " + e.getMessage(), e);
        }
        return usuario;
    }
}