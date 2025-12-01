package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Disciplina;
import org.example.model.Questao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestaoDAO {

    // 1. Cadastra uma questão no "Banco Geral" vinculado à DISCIPLINA
    public void salvar(Questao q) {
        String sql = "INSERT INTO Questao (enunciado, tipo, id_disciplina) VALUES (?, ?, ?) RETURNING id_questao";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, q.getEnunciado());
            stmt.setString(2, q.getTipo() != null ? q.getTipo() : "Discursiva");
            stmt.setInt(3, q.getDisciplina().getIdDisciplina());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                q.setIdQuestao(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar questão: " + e.getMessage());
        }
    }

    // 2. Vincula uma questão já existente a uma Prova (Salva na Tabela de Ligação)
    public void vincularQuestaoNaProva(int idAvaliacao, int idQuestao) {
        String sql = "INSERT INTO Prova_Questao (id_avaliacao, id_questao) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAvaliacao);
            stmt.setInt(2, idQuestao);
            stmt.executeUpdate();

        } catch (SQLException e) {
            // Se tentar vincular a mesma questão 2x, pode dar erro de chave duplicada
            e.printStackTrace();
        }
    }

    // MÉTODOS PARA O JSP "MONTAR PROVA"

    public List<Questao> buscarQuestoesDisponiveis(int idDisciplina, int idAvaliacao) {
        String sql = "SELECT * FROM Questao " +
                "WHERE id_disciplina = ? " +
                "AND id_questao NOT IN (" +
                "    SELECT id_questao FROM Prova_Questao WHERE id_avaliacao = ?" +
                ") ORDER BY id_questao DESC";

        List<Questao> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDisciplina);
            stmt.setInt(2, idAvaliacao);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Questao q = new Questao();
                    q.setIdQuestao(rs.getInt("id_questao"));
                    q.setEnunciado(rs.getString("enunciado"));
                    q.setTipo(rs.getString("tipo"));

                    Disciplina d = new Disciplina();
                    d.setIdDisciplina(idDisciplina);
                    q.setDisciplina(d);

                    lista.add(q);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Questao> buscarQuestoesDaProva(int idAvaliacao) {
        String sql = "SELECT q.* FROM Questao q " +
                "JOIN Prova_Questao pq ON q.id_questao = pq.id_questao " +
                "WHERE pq.id_avaliacao = ? " +
                "ORDER BY q.id_questao ASC";

        List<Questao> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAvaliacao);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Questao q = new Questao();
                    q.setIdQuestao(rs.getInt("id_questao"));
                    q.setEnunciado(rs.getString("enunciado"));
                    q.setTipo(rs.getString("tipo"));

                    lista.add(q);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}