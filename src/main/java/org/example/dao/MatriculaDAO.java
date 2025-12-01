package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Disciplina;
import org.example.model.Estudante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; // Importe o ResultSet
import java.sql.SQLException;
import java.util.ArrayList; // Importe o ArrayList
import java.util.List; // Importe o List

public class MatriculaDAO {

    //Matricula um estudante em uma disciplina.
    public void matricular(Estudante estudante, Disciplina disciplina) {
        String sql = "INSERT INTO Est_part_discp (id_usuario, id_disciplina) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, estudante.getIdUsuario());
            stmt.setInt(2, disciplina.getIdDisciplina());
            stmt.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Erro ao matricular estudante: " + e.getMessage(), e);
        }
    }

    //MÉTODO_DE_RELATÓRIO_ADICIONADO
    public List<Estudante> findEstudantesByDisciplina(int idDisciplina) {
        String sql = "SELECT u.id_usuario, u.pnome, u.snome, u.email " +
                "FROM Usuario u " +
                "JOIN Estudante e ON u.id_usuario = e.id_usuario " +
                "JOIN Est_part_discp mat ON e.id_usuario = mat.id_usuario " +
                "WHERE mat.id_disciplina = ?";

        List<Estudante> estudantes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDisciplina);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Estudante estudante = new Estudante();
                    estudante.setIdUsuario(rs.getInt("id_usuario"));
                    estudante.setPnome(rs.getString("pnome"));
                    estudante.setSnome(rs.getString("snome"));
                    estudante.setEmail(rs.getString("email"));
                    estudantes.add(estudante);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar alunos da disciplina: " + e.getMessage(), e);
        }

        return estudantes;
    }
}