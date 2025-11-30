package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Estudante;
import org.example.model.RankingDTO;
import org.example.model.Trabalho;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class TrabalhoDAO {

    public void salvar(Trabalho trabalho) {
        String sql = "INSERT INTO Trabalho (id_avaliacao, id_usuario_est, data_envio, arquivo_url, status) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id_trabalho";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, trabalho.getAvaliacao().getIdAvaliacao());
            stmt.setInt(2, trabalho.getEstudante().getIdUsuario());
            stmt.setTimestamp(3, Timestamp.valueOf(trabalho.getDataEnvio()));
            stmt.setString(4, trabalho.getArquivoUrl());
            stmt.setString(5, trabalho.getStatus());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                trabalho.setIdTrabalho(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar trabalho: " + e.getMessage(), e);
        }
    }

    public List<Trabalho> findTrabalhosByAvaliacao(int idAvaliacao) {
        String sql = "SELECT t.id_trabalho, t.arquivo_url, t.status, t.data_envio, " +
                "       u.id_usuario, u.pnome, u.snome " +
                "FROM Trabalho t " +
                "JOIN Estudante e ON t.id_usuario_est = e.id_usuario " +
                "JOIN Usuario u ON e.id_usuario = u.id_usuario " +
                "WHERE t.id_avaliacao = ? " +
                "ORDER BY t.data_envio";

        List<Trabalho> trabalhos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAvaliacao);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Estudante estudante = new Estudante();
                    estudante.setIdUsuario(rs.getInt("id_usuario"));
                    estudante.setPnome(rs.getString("pnome"));
                    estudante.setSnome(rs.getString("snome"));

                    Trabalho trabalho = new Trabalho();
                    trabalho.setIdTrabalho(rs.getInt("id_trabalho"));
                    trabalho.setArquivoUrl(rs.getString("arquivo_url"));
                    trabalho.setStatus(rs.getString("status"));
                    trabalho.setDataEnvio(rs.getTimestamp("data_envio").toLocalDateTime());

                    trabalho.setEstudante(estudante);

                    trabalhos.add(trabalho);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar trabalhos da avaliação: " + e.getMessage(), e);
        }

        return trabalhos;
    }

    // ==================================================================
    // ▼▼▼ RELATÓRIO FINAL (RANKING) - TRAVADO EM 10.0 ▼▼▼
    // ==================================================================
    public List<RankingDTO> getRankingAvaliacao(int idAvaliacao) {

        // Garante que a nota da prova não passe de 10
        String sql = "WITH NotasAluno AS (" +
                "    SELECT " +
                "        t.id_trabalho, " +
                "        u.id_usuario AS id_aluno, " +
                "        u.pnome || ' ' || COALESCE(u.snome, '') AS nome_estudante, " +
                "        (SELECT LEAST(SUM(r.nota_obtida), 10.0) FROM Resposta r WHERE r.id_estudante = u.id_usuario AND r.id_avaliacao = ?) AS nota_final " +
                "    FROM Trabalho t " +
                "    JOIN Estudante e ON t.id_usuario_est = e.id_usuario " +
                "    JOIN Usuario u ON e.id_usuario = u.id_usuario " +
                "    WHERE t.id_avaliacao = ? " +
                ")" +
                "SELECT " +
                "    RANK() OVER (ORDER BY na.nota_final DESC NULLS LAST) AS rank, " +
                "    na.nome_estudante, " +
                "    na.id_aluno, " +
                "    COALESCE(na.nota_final, 0) AS nota_final, " +
                "    na.id_trabalho " +
                "FROM NotasAluno na " +
                "ORDER BY rank";

        List<RankingDTO> ranking = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAvaliacao);
            stmt.setInt(2, idAvaliacao);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RankingDTO dto = new RankingDTO();
                    dto.setRank(rs.getLong("rank"));
                    dto.setNomeEstudante(rs.getString("nome_estudante"));
                    dto.setIdAluno(rs.getInt("id_aluno"));

                    double nota = rs.getDouble("nota_final");
                    dto.setNotaFinal(BigDecimal.valueOf(nota));

                    dto.setIdTrabalho(rs.getInt("id_trabalho"));
                    ranking.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao gerar ranking da avaliação: " + e.getMessage(), e);
        }

        return ranking;
    }

    // ==================================================================
    // ▼▼▼ MÉDIA GERAL (MÉDIA SIMPLES) ▼▼▼
    // ==================================================================
    public Map<String, BigDecimal> getOverallFinalAverage() {

        // VOLTOU PARA AVG (Média Simples)
        // Calcula a média das provas (já travadas em 10)
        String sql = "SELECT u.pnome || ' ' || COALESCE(u.snome, '') AS nome_aluno, " +
                "       AVG( " +
                "           LEAST(total_por_prova.soma_notas, 10.0) " +
                "       ) AS media_geral " +
                "FROM (" +
                "    SELECT id_estudante, id_avaliacao, SUM(nota_obtida) as soma_notas " +
                "    FROM Resposta " +
                "    GROUP BY id_estudante, id_avaliacao " +
                ") AS total_por_prova " +
                "JOIN Usuario u ON total_por_prova.id_estudante = u.id_usuario " +
                "GROUP BY u.pnome, u.snome " +
                "ORDER BY media_geral DESC";

        Map<String, BigDecimal> medias = new LinkedHashMap<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome_aluno");
                BigDecimal media = rs.getBigDecimal("media_geral");

                if (media != null) {
                    medias.put(nome, media.setScale(2, BigDecimal.ROUND_HALF_UP));
                } else {
                    medias.put(nome, BigDecimal.ZERO);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao gerar média global: " + e.getMessage(), e);
        }
        return medias;
    }
}