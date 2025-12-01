package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Criterio;

import java.math.BigDecimal; // <-- IMPORT ADICIONADO
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; // <-- IMPORT ADICIONADO
import java.util.List; // <-- IMPORT ADICIONADO

public class CriterioDAO {

    public void salvar(Criterio criterio) {
        String sql = "INSERT INTO Criterio (id_avaliacao, nome, descricao, peso) " +
                "VALUES (?, ?, ?, ?) RETURNING id_criterio";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, criterio.getAvaliacao().getIdAvaliacao());
            stmt.setString(2, criterio.getNome());
            stmt.setString(3, criterio.getDescricao());
            stmt.setBigDecimal(4, criterio.getPeso()); // Passa o BigDecimal

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                criterio.setIdCriterio(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar critério: " + e.getMessage(), e);
        }
    }

    public List<Criterio> findCriteriosByAvaliacao(int idAvaliacao) {
        String sql = "SELECT * FROM Criterio WHERE id_avaliacao = ?";
        List<Criterio> criterios = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAvaliacao);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Criterio c = new Criterio();
                    c.setIdCriterio(rs.getInt("id_criterio"));
                    c.setNome(rs.getString("nome"));
                    c.setDescricao(rs.getString("descricao"));
                    c.setPeso(rs.getBigDecimal("peso"));

                    criterios.add(c);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar critérios: " + e.getMessage(), e);
        }
        return criterios;
    }


    public List<Criterio> findCriteriosByTrabalhoId(int idTrabalho) {
        String sql = "SELECT c.* " +
                "FROM Criterio c " +
                "JOIN Avaliacao a ON c.id_avaliacao = a.id_avaliacao " +
                "JOIN Trabalho t ON a.id_avaliacao = t.id_avaliacao " +
                "WHERE t.id_trabalho = ?"; // Filtra pelo ID do Trabalho

        List<Criterio> criterios = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTrabalho);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Criterio c = new Criterio();
                    c.setIdCriterio(rs.getInt("id_criterio"));
                    c.setNome(rs.getString("nome"));
                    c.setDescricao(rs.getString("descricao"));
                    c.setPeso(rs.getBigDecimal("peso"));

                    criterios.add(c);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar critérios pelo ID do trabalho: " + e.getMessage(), e);
        }
        return criterios;
    }
}