package org.example.controller;

import org.example.dao.AvaliacaoDAO;
import org.example.dao.QuestaoDAO;
import org.example.model.Avaliacao;
import org.example.model.Questao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/montarProva")
public class MontarProvaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Pega o ID da avaliação que veio da tela anterior
        String idParam = req.getParameter("idAvaliacao");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int idAvaliacao = Integer.parseInt(idParam);

                // 2. Busca os dados da Avaliação
                AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();
                Avaliacao avaliacao = avaliacaoDAO.buscarPorId(idAvaliacao);

                if (avaliacao != null) {
                    QuestaoDAO questaoDAO = new QuestaoDAO();


                    // 3. Busca questões DISPONÍVEIS
                    // Passamos (ID da Disciplina, ID da Prova) para filtrar as que já foram usadas
                    List<Questao> questoesDisponiveis = questaoDAO.buscarQuestoesDisponiveis(
                            avaliacao.getDisciplina().getIdDisciplina(),
                            idAvaliacao
                    );

                    // 4. Busca questões JÁ ADICIONADAS
                    List<Questao> questoesNaProva = questaoDAO.buscarQuestoesDaProva(idAvaliacao);

                    // 5. Manda tudo para o JSP
                    req.setAttribute("avaliacao", avaliacao);

                    // OBS os nomes aqui, devem ser iguais aos usados no JSP:
                    req.setAttribute("questoesDisponiveis", questoesDisponiveis);
                    req.setAttribute("questoesNaProva", questoesNaProva);

                    // 6. Abre a tela
                    req.getRequestDispatcher("montar_prova.jsp").forward(req, resp);
                    return;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        // Se ocorrer algum erro, volta pro inicio
        resp.sendRedirect("index.jsp");
    }
}