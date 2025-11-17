package org.example.controller;

import org.example.dao.AvaliacaoDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Este Servlet vai ouvir o link do JSP
@WebServlet("/deletarAvaliacao")
public class DeletarAvaliacaoServlet extends HttpServlet {

    private AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Pega o ID da URL (que vem do link do JSP)
            int idAvaliacao = Integer.parseInt(request.getParameter("id"));

            // 2. Chama o método DELETE que acabamos de criar
            avaliacaoDAO.deletar(idAvaliacao);

            // 3. Redireciona de volta para a lista de avaliações
            response.sendRedirect("avaliacoes.jsp");

        } catch (Exception e) {
            // Lança um erro (para o Tomcat mostrar a página 500 com o stack trace)
            throw new ServletException("Erro ao deletar avaliação", e);
        }
    }
}
