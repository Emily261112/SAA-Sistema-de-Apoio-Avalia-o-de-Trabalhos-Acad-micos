package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Questao;
import org.example.model.Resposta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RespostaDAO {

    // 1. Busca as respostas de UM aluno em UMA prova específica
    // Traz junto o enunciado da questão para o professor ler
    public List<Resposta> buscarRespostasDoAluno(int idAvaliacao, int idAluno) {
        String sql = "SELECT r.id_resposta, r.resposta_texto, r.nota_obtida, " +
                "       q.enunciado, q.tipo " +
                "FROM Resposta r " +
                "JOIN Questao q ON r.id_questao = q.id_questao " +
                "WHERE r.id_avaliacao = ? AND r.id_estudante = ? " +
                "ORDER BY q.id_questao";

        List<Resposta> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAvaliacao);
            stmt.setInt(2, idAluno);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Resposta r = new Resposta();
                    r.setIdResposta(rs.getInt("id_resposta"));
                    r.setRespostaTexto(rs.getString("resposta_texto"));
                    r.setNotaObtida(rs.getDouble("nota_obtida"));

                    // Monta a questão dentro da resposta para exibir o enunciado
                    Questao q = new Questao();
                    q.setEnunciado(rs.getString("enunciado"));
                    q.setTipo(rs.getString("tipo"));
                    r.setQuestao(q);

                    lista.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 2. Atualiza a nota de uma resposta específica
    public void atualizarNota(int idResposta, double novaNota) {
        String sql = "UPDATE Resposta SET nota_obtida = ? WHERE id_resposta = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, novaNota);
            stmt.setInt(2, idResposta);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}