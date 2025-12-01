package org.example.controller;

import org.example.dao.QuestaoDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/vincularQuestao")
public class VincularQuestaoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Pega o ID da Avaliação
        String idAvaliacaoStr = req.getParameter("idAvaliacao");

        // 2. Pega os IDs das questões que foram marcadas
        String[] idsSelecionados = req.getParameterValues("idsSelecionados");

        if (idAvaliacaoStr != null && idsSelecionados != null) {
            int idAvaliacao = Integer.parseInt(idAvaliacaoStr);
            QuestaoDAO dao = new QuestaoDAO();

            // 3. Percorre cada checkbox marcado e salva na tabela de ligação
            for (String idQuestaoStr : idsSelecionados) {
                int idQuestao = Integer.parseInt(idQuestaoStr);
                dao.vincularQuestaoNaProva(idAvaliacao, idQuestao);
            }
        }

        // 4. Volta para a mesma tela de montagem para continuar editando
        resp.sendRedirect("montarProva?idAvaliacao=" + idAvaliacaoStr);
    }
}