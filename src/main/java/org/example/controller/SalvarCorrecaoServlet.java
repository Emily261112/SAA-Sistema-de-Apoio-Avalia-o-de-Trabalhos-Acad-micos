package org.example.controller;

import org.example.dao.RespostaDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/salvarCorrecao")
public class SalvarCorrecaoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idAvaliacao = req.getParameter("idAvaliacao");
        RespostaDAO dao = new RespostaDAO();

        // Percorre todos os parâmetros enviados pelo formulário
        Map<String, String[]> parametros = req.getParameterMap();

        for (String nomeParametro : parametros.keySet()) {
            // Se o campo começa com "nota_", é uma nota de resposta
            if (nomeParametro.startsWith("nota_")) {
                try {
                    // Extrai o ID da resposta do nome (ex: "nota_55" -> 55)
                    int idResposta = Integer.parseInt(nomeParametro.split("_")[1]);

                    // Pega o valor digitado
                    double nota = Double.parseDouble(req.getParameter(nomeParametro));

                    // Atualiza no banco
                    dao.atualizarNota(idResposta, nota);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Volta para a lista de alunos
        resp.sendRedirect("lancar_nota.jsp?id=" + idAvaliacao);
    }
}