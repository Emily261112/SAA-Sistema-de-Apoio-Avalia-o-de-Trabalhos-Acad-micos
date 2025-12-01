package org.example.controller;

import org.example.dao.AvaliacaoDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deletarAvaliacao")
public class DeletarAvaliacaoServlet extends HttpServlet {

    private AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Pega o ID da URL
            int idAvaliacao = Integer.parseInt(request.getParameter("id"));

            // 2. Chama o método_delete
            avaliacaoDAO.deletar(idAvaliacao);

            // 3. Redireciona de volta para a lista de avaliações
            response.sendRedirect("avaliacoes.jsp");

        } catch (Exception e) {
            throw new ServletException("Erro ao deletar avaliação", e);
        }
    }
}
