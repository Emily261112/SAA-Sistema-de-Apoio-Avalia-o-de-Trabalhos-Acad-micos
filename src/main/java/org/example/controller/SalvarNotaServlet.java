package org.example.controller;

import org.example.dao.NotaCriterioDAO;
import org.example.model.Criterio;
import org.example.model.NotaCriterio;
import org.example.model.Trabalho;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/salvarNota")
public class SalvarNotaServlet extends HttpServlet {

    private NotaCriterioDAO notaDAO = new NotaCriterioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Pega o ID do trabalho
            int idTrabalho = Integer.parseInt(request.getParameter("idTrabalho"));

            // Pega a LISTA de IDs de critérios que estavam no form
            String[] idsCriterios = request.getParameterValues("idCriterio");

            if (idsCriterios != null) {
                // Faz um loop por CADA critério que estava na página
                for (String idCriterioStr : idsCriterios) {
                    int idCriterio = Integer.parseInt(idCriterioStr);

                    // Pega os dados específicos daquele critério
                    String nomeParamNota = "nota_" + idCriterio;
                    String nomeParamObs = "obs_" + idCriterio;

                    BigDecimal notaAtribuida = new BigDecimal(request.getParameter(nomeParamNota));
                    String observacao = request.getParameter(nomeParamObs);

                    // Monta o objeto NotaCriterio
                    NotaCriterio nota = new NotaCriterio();
                    nota.setNotaAtribuida(notaAtribuida);
                    nota.setObservacao(observacao);

                    // Monta os "stubs" das chaves estrangeiras
                    Trabalho t = new Trabalho();
                    t.setIdTrabalho(idTrabalho);
                    Criterio c = new Criterio();
                    c.setIdCriterio(idCriterio);

                    nota.setTrabalho(t);
                    nota.setCriterio(c);

                    // Salva no banco!
                    notaDAO.salvar(nota);
                }
            }

            // Redireciona de volta para a lista de avaliações
            response.sendRedirect("avaliacoes.jsp");

        } catch (Exception e) {
            throw new ServletException("Erro ao salvar notas", e);
        }
    }
}