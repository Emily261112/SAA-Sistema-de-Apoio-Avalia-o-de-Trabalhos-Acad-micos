package org.example.controller;

import org.example.dao.QuestaoDAO;
import org.example.model.Disciplina;
import org.example.model.Questao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/salvarNovaQuestao")
public class SalvarNovaQuestaoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Recebe os dados do formulário
        String enunciado = req.getParameter("enunciado");
        String tipo = req.getParameter("tipo");
        int idAvaliacao = Integer.parseInt(req.getParameter("idAvaliacao"));
        int idDisciplina = Integer.parseInt(req.getParameter("idDisciplina"));

        // 2. Monta o objeto Questao
        Questao q = new Questao();
        q.setEnunciado(enunciado);
        q.setTipo(tipo);

        // Define a disciplina (Necessário pois mudamos o banco para vincular à disciplina)
        Disciplina d = new Disciplina();
        d.setIdDisciplina(idDisciplina);
        q.setDisciplina(d);

        QuestaoDAO dao = new QuestaoDAO();

        // 3. Salva no Banco Geral (Aqui o objeto 'q' ganha um ID gerado pelo banco)
        dao.salvar(q);

        // 4. Pega esse ID novo e já vincula nesta prova
        if (q.getIdQuestao() > 0) {
            dao.vincularQuestaoNaProva(idAvaliacao, q.getIdQuestao());
        }

        // 5. Atualiza a página
        resp.sendRedirect("montarProva?idAvaliacao=" + idAvaliacao);
    }
}