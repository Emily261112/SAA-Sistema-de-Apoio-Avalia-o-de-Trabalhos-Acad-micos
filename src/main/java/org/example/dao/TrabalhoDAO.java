package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Estudante;
import org.example.model.RankingDTO; // <-- IMPORT ADICIONADO
import org.example.model.Trabalho;

import java.math.BigDecimal; // <-- IMPORT ADICIONADO
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map; // <-- NOVO IMPORT
import java.util.HashMap; // <-- NOVO IMPORT
import java.util.LinkedHashMap; // <-- NOVO IMPORT

public class TrabalhoDAO {

    public void salvar(Trabalho trabalho) {
        String sql = "INSERT INTO Trabalho (id_avaliacao, id_usuario_est, data_envio, arquivo_url, status) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id_trabalho";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, trabalho.getAvaliacao().getIdAvaliacao());
            stmt.setInt(2, trabalho.getEstudante().getIdUsuario());

            // Converte LocalDateTime para Timestamp
            stmt.setTimestamp(3, Timestamp.valueOf(trabalho.getDataEnvio()));

            stmt.setString(4, trabalho.getArquivoUrl());
            stmt.setString(5, trabalho.getStatus());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                trabalho.setIdTrabalho(rs.getInt(1));
            }

        } catch (SQLException e) {
            // Vai falhar se o aluno tentar enviar duas vezes (UNIQUE constraint)
            throw new RuntimeException("Erro ao salvar trabalho: " + e.getMessage(), e);
        }
    }

    /**
     * RELATÓRIO: Lista todos os trabalhos enviados para uma avaliação específica.
     */
    public java.util.List<Trabalho> findTrabalhosByAvaliacao(int idAvaliacao) {
        String sql = "SELECT t.id_trabalho, t.arquivo_url, t.status, t.data_envio, " +
                "       u.id_usuario, u.pnome, u.snome " +
                "FROM Trabalho t " +
                "JOIN Estudante e ON t.id_usuario_est = e.id_usuario " +
                "JOIN Usuario u ON e.id_usuario = u.id_usuario " +
                "WHERE t.id_avaliacao = ? " +
                "ORDER BY t.data_envio"; // Ordena por data de envio

        java.util.List<Trabalho> trabalhos = new java.util.ArrayList<>();

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
    // ▼▼▼ RELATÓRIO FINAL (RANKING) ▼▼▼
    // ==================================================================
    /**
     * RELATÓRIO FINAL: Gera o ranking de notas para uma avaliação.
     */
    public List<RankingDTO> getRankingAvaliacao(int idAvaliacao) {

        String sql = "WITH NotasTrabalho AS (" +
                "    SELECT " +
                "        t.id_trabalho, " +
                "        u.pnome || ' ' || u.snome AS nome_estudante, " +
                "        SUM(nc.nota_atribuida) AS nota_final " +
                "    FROM Trabalho t " +
                "    JOIN Estudante e ON t.id_usuario_est = e.id_usuario " +
                "    JOIN Usuario u ON e.id_usuario = u.id_usuario " +
                "    LEFT JOIN Nota_Criterio nc ON t.id_trabalho = nc.id_trabalho " +
                "    WHERE t.id_avaliacao = ? " +
                "    GROUP BY t.id_trabalho, u.pnome, u.snome " +
                ")" +
                "SELECT " +
                "    RANK() OVER (ORDER BY nt.nota_final DESC NULLS LAST) AS rank, " +
                "    nt.nome_estudante, " +
                "    COALESCE(nt.nota_final, 0) AS nota_final, " + // COALESCE troca NULL por 0
                "    nt.id_trabalho " +
                "FROM NotasTrabalho nt " +
                "ORDER BY rank";

        List<RankingDTO> ranking = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAvaliacao);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RankingDTO dto = new RankingDTO();
                    dto.setRank(rs.getLong("rank"));
                    dto.setNomeEstudante(rs.getString("nome_estudante"));
                    dto.setNotaFinal(rs.getBigDecimal("nota_final"));
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
    // ▼▼▼ NOVO MÉTODO: MÉDIA GERAL (ADICIONADO) ▼▼▼
    // ==================================================================
    /**
     * RELATÓRIO FINAL: Calcula a Média Geral das notas de um aluno em todas as APs.
     * Usa subconsultas (CTE), Agregação (SUM, AVG) e múltiplos JOINs.
     * Retorna Map<Nome do Aluno, Média Final>.
     */
    public Map<String, BigDecimal> getOverallFinalAverage() {
        // 1. CTE para somar as notas de cada trabalho (AP1, AP2...)
        String sql = "WITH TrabalhoNota AS (" +
                "    SELECT id_trabalho, SUM(nota_atribuida) AS nota_total_trabalho " +
                "    FROM Nota_Criterio " +
                "    GROUP BY id_trabalho" +
                ") " +
                // 2. Query principal que calcula a MÉDIA (AVG) dessas notas totais
                "SELECT u.pnome || ' ' || u.snome AS nome_aluno, " +
                "       AVG(tn.nota_total_trabalho) AS media_geral " +
                "FROM TrabalhoNota tn " +
                "JOIN Trabalho t ON tn.id_trabalho = t.id_trabalho " +
                "JOIN Estudante e ON t.id_usuario_est = e.id_usuario " +
                "JOIN Usuario u ON e.id_usuario = u.id_usuario " +
                "GROUP BY u.pnome, u.snome " + // Agrupa por aluno
                "ORDER BY media_geral DESC"; // Ordena do melhor para o pior

        // Usamos LinkedHashMap para manter a ordem do ranking (melhor para o pior)
        Map<String, BigDecimal> medias = new LinkedHashMap<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome_aluno");
                BigDecimal media = rs.getBigDecimal("media_geral");
                // Arredonda a média para 2 casas decimais
                medias.put(nome, media.setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao gerar média global: " + e.getMessage(), e);
        }
        return medias;
    }
}