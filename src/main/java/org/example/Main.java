package org.example;

import org.example.dao.TrabalhoDAO;
import org.example.model.RankingDTO; // Importe o DTO
import java.util.List;

public class Main {
    public static void main(String[] args) {

        TrabalhoDAO trabalhoDAO = new TrabalhoDAO();

        try {
            System.out.println("--- RELATÓRIO FINAL: Ranking da Avaliação AP1 (ID 1) ---");

            // O ID 1 é a nossa "AP1"
            List<RankingDTO> ranking = trabalhoDAO.getRankingAvaliacao(1);

            if (ranking.isEmpty()) {
                System.out.println("Nenhum trabalho para rankear.");
            } else {
                System.out.println("Ranking de Notas Finais:");
                for (RankingDTO item : ranking) {
                    System.out.println("  Posição: " + item.getRank());
                    System.out.println("  Aluno: " + item.getNomeEstudante());
                    System.out.println("  Nota Final: " + item.getNotaFinal());
                    System.out.println("  ------------------");
                }
            }

        } catch (Exception e) {
            System.err.println("!!! FALHA AO GERAR RELATÓRIO !!!");
            e.printStackTrace();
        }
    }
}