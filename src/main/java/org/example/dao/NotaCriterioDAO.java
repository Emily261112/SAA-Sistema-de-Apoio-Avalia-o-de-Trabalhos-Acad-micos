package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.NotaCriterio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotaCriterioDAO {

    public void salvar(NotaCriterio nota) {
        String sql = "INSERT INTO Nota_Criterio (id_trabalho, id_criterio, nota_atribuida, observacao) " +
                "VALUES (?, ?, ?, ?) RETURNING id_nota_criterio";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nota.getTrabalho().getIdTrabalho());
            stmt.setInt(2, nota.getCriterio().getIdCriterio());
            stmt.setBigDecimal(3, nota.getNotaAtribuida());
            stmt.setString(4, nota.getObservacao());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nota.setIdNotaCriterio(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar nota: " + e.getMessage(), e);
        }
    }
}