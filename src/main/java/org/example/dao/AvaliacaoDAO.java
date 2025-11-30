package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Avaliacao;
import org.example.model.Disciplina;
import org.example.model.Professor;
import org.example.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvaliacaoDAO {

    public void salvar(Avaliacao avaliacao) {
        // ATUALIZADO: Agora salva o max_questoes também
        String sql = "INSERT INTO Avaliacao (id_disciplina, cria_aval_prof_id, cod_avaliacao, data_publicacao, prazo, max_questoes) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id_avaliacao";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, avaliacao.getDisciplina().getIdDisciplina());
            stmt.setInt(2, avaliacao.getProfessorCriador().getIdUsuario());
            stmt.setString(3, avaliacao.getCodAvaliacao());
            stmt.setDate(4, Date.valueOf(avaliacao.getDataPublicacao()));
            stmt.setDate(5, Date.valueOf(avaliacao.getPrazo()));

            // Define um padrão de 10 se estiver zerado
            int maxQ = avaliacao.getMaxQuestoes() > 0 ? avaliacao.getMaxQuestoes() : 10;
            stmt.setInt(6, maxQ);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                avaliacao.setIdAvaliacao(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar avaliação: " + e.getMessage(), e);
        }
    }

    public Avaliacao buscarPorId(int idAvaliacao) {
        String sql = "SELECT a.id_avaliacao, a.cod_avaliacao, a.prazo, a.max_questoes, " +
                "       d.id_disciplina, d.nome AS nome_disciplina " +
                "FROM Avaliacao a " +
                "JOIN Disciplina d ON a.id_disciplina = d.id_disciplina " +
                "WHERE a.id_avaliacao = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAvaliacao);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Avaliacao av = new Avaliacao();
                    av.setIdAvaliacao(rs.getInt("id_avaliacao"));
                    av.setCodAvaliacao(rs.getString("cod_avaliacao"));

                    if (rs.getDate("prazo") != null) {
                        av.setPrazo(rs.getDate("prazo").toLocalDate());
                    }

                    // Tenta pegar o max_questoes
                    try { av.setMaxQuestoes(rs.getInt("max_questoes")); } catch (Exception e) {}

                    Disciplina d = new Disciplina();
                    d.setIdDisciplina(rs.getInt("id_disciplina"));
                    d.setNome(rs.getString("nome_disciplina"));

                    av.setDisciplina(d);

                    return av;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar avaliação por ID: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Avaliacao> findAll() {
        String sql = "SELECT a.id_avaliacao, a.cod_avaliacao, a.prazo, " +
                "       d.nome AS nome_disciplina, " +
                "       u.pnome AS nome_professor " +
                "FROM Avaliacao a " +
                "JOIN Disciplina d ON a.id_disciplina = d.id_disciplina " +
                "JOIN Professor p ON a.cria_aval_prof_id = p.id_usuario " +
                "JOIN Usuario u ON p.id_usuario = u.id_usuario " +
                "ORDER BY a.prazo DESC";

        List<Avaliacao> avaliacoes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Disciplina d = new Disciplina();
                d.setNome(rs.getString("nome_disciplina"));

                Professor p = new Professor();
                p.setPnome(rs.getString("nome_professor"));

                Avaliacao av = new Avaliacao();
                av.setIdAvaliacao(rs.getInt("id_avaliacao"));
                av.setCodAvaliacao(rs.getString("cod_avaliacao"));
                av.setPrazo(rs.getDate("prazo").toLocalDate());
                av.setDisciplina(d);
                av.setProfessorCriador(p);

                avaliacoes.add(av);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar avaliações: " + e.getMessage(), e);
        }

        return avaliacoes;
    }

    public List<Avaliacao> findAvaliacoesByProfessor(int idProfessor) {
        String sql = "SELECT a.id_avaliacao, a.cod_avaliacao, a.prazo, " +
                "       d.nome AS nome_disciplina, " +
                "       u.pnome AS nome_professor " +
                "FROM Avaliacao a " +
                "JOIN Disciplina d ON a.id_disciplina = d.id_disciplina " +
                "JOIN Professor p ON a.cria_aval_prof_id = p.id_usuario " +
                "JOIN Usuario u ON p.id_usuario = u.id_usuario " +
                "WHERE a.cria_aval_prof_id = ? " +
                "ORDER BY a.prazo DESC";

        List<Avaliacao> avaliacoes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProfessor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Disciplina d = new Disciplina();
                    d.setNome(rs.getString("nome_disciplina"));

                    Professor prof = new Professor();
                    prof.setPnome(rs.getString("nome_professor"));

                    Avaliacao av = new Avaliacao();
                    av.setIdAvaliacao(rs.getInt("id_avaliacao"));
                    av.setCodAvaliacao(rs.getString("cod_avaliacao"));
                    av.setPrazo(rs.getDate("prazo").toLocalDate());
                    av.setDisciplina(d);
                    av.setProfessorCriador(prof);

                    avaliacoes.add(av);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar avaliações por professor: " + e.getMessage(), e);
        }

        return avaliacoes;
    }

    // ==================================================================
    // ▼▼▼ MÉTODO DELETAR (VERSÃO FINAL) ▼▼▼
    // Remove Respostas -> Trabalhos -> Prova_Questao -> Avaliacao
    // ==================================================================
    public void deletar(int idAvaliacao) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); // Inicia Transação

            // 1. Apaga as RESPOSTAS dos alunos
            String sqlRespostas = "DELETE FROM Resposta WHERE id_avaliacao = ?";
            stmt = conn.prepareStatement(sqlRespostas);
            stmt.setInt(1, idAvaliacao);
            stmt.executeUpdate();
            stmt.close();

            // 2. Apaga os TRABALHOS (PDFs)
            String sqlTrabalhos = "DELETE FROM Trabalho WHERE id_avaliacao = ?";
            stmt = conn.prepareStatement(sqlTrabalhos);
            stmt.setInt(1, idAvaliacao);
            stmt.executeUpdate();
            stmt.close();

            // 3. Apaga os vínculos PROVA-QUESTÃO
            String sqlLimpar = "DELETE FROM Prova_Questao WHERE id_avaliacao = ?";
            stmt = conn.prepareStatement(sqlLimpar);
            stmt.setInt(1, idAvaliacao);
            stmt.executeUpdate();
            stmt.close();

            // 4. Apaga a Avaliação
            String sqlDeletar = "DELETE FROM Avaliacao WHERE id_avaliacao = ?";
            stmt = conn.prepareStatement(sqlDeletar);
            stmt.setInt(1, idAvaliacao);
            stmt.executeUpdate();

            // 5. Confirma tudo
            conn.commit();

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Erro ao deletar avaliação e vínculos: " + e.getMessage(), e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}