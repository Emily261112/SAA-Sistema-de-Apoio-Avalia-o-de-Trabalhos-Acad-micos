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
        String sql = "INSERT INTO Disciplina (nome, adm_prof_id) VALUES (?, ?) RETURNING id_disciplina";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, disciplina.getNome());
            // No DAO, nós pegamos o ID do objeto Professor
            stmt.setInt(2, disciplina.getProfessor().getIdUsuario());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                disciplina.setIdDisciplina(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar disciplina: " + e.getMessage(), e);
        }
    }

    /**
     * Um exemplo de método para listar todas as disciplinas
     * (Isso será útil para os relatórios!)
     */
    public List<Disciplina> findAll() {
        // SQL complexo com JOIN para buscar o nome do professor
        String sql = "SELECT d.id_disciplina, d.nome, p.id_usuario, u.pnome, u.snome " +
                "FROM Disciplina d " +
                "JOIN Professor p ON d.adm_prof_id = p.id_usuario " +
                "JOIN Usuario u ON p.id_usuario = u.id_usuario";

        List<Disciplina> disciplinas = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // 1. Criar o Professor
                Professor prof = new Professor();
                prof.setIdUsuario(rs.getInt("id_usuario"));
                prof.setPnome(rs.getString("pnome"));
                prof.setSnome(rs.getString("snome"));

                // 2. Criar a Disciplina
                Disciplina disciplina = new Disciplina();
                disciplina.setIdDisciplina(rs.getInt("id_disciplina"));
                disciplina.setNome(rs.getString("nome"));

                // 3. Ligar o Professor à Disciplina
                disciplina.setProfessor(prof);

                disciplinas.add(disciplina);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar disciplinas: " + e.getMessage(), e);
        }

        return disciplinas;
    }

    // ==================================================================
    // ▼▼▼ MÉTODO DE RELATÓRIO: Conta Alunos por Disciplina ▼▼▼
    // ==================================================================
    /**
     * RELATÓRIO: Conta quantos alunos estão matriculados em cada disciplina.
     * Usa LEFT JOIN, COUNT e GROUP BY.
     * Retorna um Map onde a Chave é o Nome da Disciplina e o Valor é o total.
     */
    public java.util.Map<String, Integer> countAlunosPorDisciplina() {
        String sql = "SELECT d.nome, COUNT(mat.id_usuario) AS total_alunos " +
                "FROM Disciplina d " +
                // Usamos LEFT JOIN para incluir disciplinas com 0 alunos
                "LEFT JOIN Est_part_discp mat ON d.id_disciplina = mat.id_disciplina " +
                "GROUP BY d.id_disciplina, d.nome " + // Agrupa pela disciplina
                "ORDER BY total_alunos DESC"; // Ordena do maior para o menor

        java.util.Map<String, Integer> relatorio = new java.util.HashMap<>();

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

    // ==================================================================
    // ▼▼▼ MÉTODO DE FILTRO ESSENCIAL: Disciplinas por Professor (ADICIONADO) ▼▼▼
    // ==================================================================
    /**
     * FILTRO ESSENCIAL: Busca todas as disciplinas administradas por um professor específico.
     * (Usado no Dashboard após o login para filtrar por 'adm_prof_id').
     */
    public List<Disciplina> findDisciplinasByProfessor(int idProfessor) {
        // Seleciona apenas id e nome, filtrando pelo ID do professor (adm_prof_id)
        String sql = "SELECT id_disciplina, nome " +
                "FROM Disciplina " +
                "WHERE adm_prof_id = ?";

        List<Disciplina> disciplinas = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProfessor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Disciplina d = new Disciplina();
                    d.setIdDisciplina(rs.getInt("id_disciplina"));
                    d.setNome(rs.getString("nome"));

                    // Cria um objeto Professor (mínimo) para completar a classe Disciplina
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