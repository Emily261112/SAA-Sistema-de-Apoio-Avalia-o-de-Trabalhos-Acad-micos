package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Avaliacao;
import org.example.model.Disciplina;
import org.example.model.Professor;
import org.example.model.Usuario; // Adicionado para mapeamento (se necessário)

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AvaliacaoDAO {

    public void salvar(Avaliacao avaliacao) {
        String sql = "INSERT INTO Avaliacao (id_disciplina, cria_aval_prof_id, cod_avaliacao, data_publicacao, prazo) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id_avaliacao";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, avaliacao.getDisciplina().getIdDisciplina());
            stmt.setInt(2, avaliacao.getProfessorCriador().getIdUsuario());
            stmt.setString(3, avaliacao.getCodAvaliacao());

            // java.time.LocalDate precisa ser convertido para java.sql.Date
            stmt.setDate(4, Date.valueOf(avaliacao.getDataPublicacao()));
            stmt.setDate(5, Date.valueOf(avaliacao.getPrazo()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                avaliacao.setIdAvaliacao(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar avaliação: " + e.getMessage(), e);
        }
    }

    /**
     * RELATÓRIO: Lista todas as avaliações cadastradas.
     * (Usado para fins de administração ou testes).
     */
    public List<Avaliacao> findAll() {
        // SQL complexo com JOIN para buscar o nome do professor
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

    // ==================================================================
    // ▼▼▼ NOVO MÉTODO PARA FILTRO POR PROFESSOR (SOLUÇÃO DO PROBLEMA) ▼▼▼
    // ==================================================================
    /**
     * FILTRA Avaliações: Lista avaliações criadas por um professor específico.
     * (Usado para a página 'Avaliações Cadastradas', resolvendo o erro de visualização).
     */
    public List<Avaliacao> findAvaliacoesByProfessor(int idProfessor) {
        String sql = "SELECT a.id_avaliacao, a.cod_avaliacao, a.prazo, " +
                "       d.nome AS nome_disciplina, " +
                "       u.pnome AS nome_professor " +
                "FROM Avaliacao a " +
                "JOIN Disciplina d ON a.id_disciplina = d.id_disciplina " +
                "JOIN Professor p ON a.cria_aval_prof_id = p.id_usuario " +
                "JOIN Usuario u ON p.id_usuario = u.id_usuario " +
                "WHERE a.cria_aval_prof_id = ? " + // <-- O FILTRO ESSENCIAL
                "ORDER BY a.prazo DESC";

        List<Avaliacao> avaliacoes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProfessor); // Define o ID do professor no filtro

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
    // ▼▼▼ MÉTODO DELETE ADICIONADO PARA O CRUD ▼▼▼
    // ==================================================================
    /**
     * Deleta uma Avaliação pelo ID.
     * Usa CASCADE para limpar as tabelas filhas (Critério, Nota_Criterio, etc.).
     */
    public void deletar(int idAvaliacao) {
        // O comando DELETE apaga a avaliação e as tabelas filhas são limpas
        // graças à regra ON DELETE CASCADE no banco.
        String sql = "DELETE FROM Avaliacao WHERE id_avaliacao = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAvaliacao);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar avaliação: " + e.getMessage(), e);
        }
    }
}