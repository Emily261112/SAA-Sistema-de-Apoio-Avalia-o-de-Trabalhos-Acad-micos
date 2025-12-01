package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Disciplina;
import org.example.model.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DisciplinaDAO {

    public void salvar(Disciplina disciplina) {
        String sql = "INSERT INTO Disciplina (nome, id_professor) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, disciplina.getNome());

            stmt.setInt(2, disciplina.getProfessor().getIdUsuario());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar disciplina: " + e.getMessage(), e);
        }
    }
      //MÉTODO_PARA_LISTAR_TODAS_AS_DISCIPLINAS

    public List<Disciplina> findAll() {
        String sql = "SELECT d.id_disciplina, d.nome, p.id_usuario, u.pnome, u.snome " +
                "FROM Disciplina d " +
                "JOIN Professor p ON d.id_professor = p.id_usuario " +
                "JOIN Usuario u ON p.id_usuario = u.id_usuario";

        List<Disciplina> disciplinas = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Professor prof = new Professor();
                prof.setIdUsuario(rs.getInt("id_usuario"));
                prof.setPnome(rs.getString("pnome"));
                prof.setSnome(rs.getString("snome"));

                Disciplina disciplina = new Disciplina();
                disciplina.setIdDisciplina(rs.getInt("id_disciplina"));
                disciplina.setNome(rs.getString("nome"));
                disciplina.setProfessor(prof);

                disciplinas.add(disciplina);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar disciplinas: " + e.getMessage(), e);
        }

        return disciplinas;
    }

    //MÉTODO_DE_RELATÓRIO: Conta Alunos por Disciplina
    public Map<String, Integer> countAlunosPorDisciplina() {
        String sql = "SELECT d.nome, COUNT(mat.id_usuario) AS total_alunos " +
                "FROM Disciplina d " +
                "LEFT JOIN Est_part_discp mat ON d.id_disciplina = mat.id_disciplina " +
                "GROUP BY d.id_disciplina, d.nome " +
                "ORDER BY total_alunos DESC";

        Map<String, Integer> relatorio = new HashMap<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nomeDisciplina = rs.getString("nome");
                int totalAlunos = rs.getInt("total_alunos");
                relatorio.put(nomeDisciplina, totalAlunos);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao gerar relatório de alunos por disciplina: " + e.getMessage(), e);
        }

        return relatorio;
    }


    // MÉTODO_DE_FILTRO_ESSENCIAL: Disciplinas por Professor
    public List<Disciplina> findDisciplinasByProfessor(int idProfessor) {

        String sql = "SELECT id_disciplina, nome " +
                "FROM Disciplina " +
                "WHERE id_professor = ?";

        List<Disciplina> disciplinas = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProfessor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Disciplina d = new Disciplina();
                    d.setIdDisciplina(rs.getInt("id_disciplina"));
                    d.setNome(rs.getString("nome"));

                    Professor prof = new Professor();
                    prof.setIdUsuario(idProfessor);
                    d.setProfessor(prof);

                    disciplinas.add(d);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar disciplinas por professor: " + e.getMessage(), e);
        }
        return disciplinas;
    }
}