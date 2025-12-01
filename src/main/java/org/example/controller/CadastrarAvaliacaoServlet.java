package org.example.controller;

import org.example.dao.AvaliacaoDAO;
import org.example.model.Avaliacao;
import org.example.model.Disciplina;
import org.example.model.Professor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/cadastrarAvaliacao")
public class CadastrarAvaliacaoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // 1. Recebe os dados do formulário
            String idDisciplinaStr = req.getParameter("disciplina");
            String codigo = req.getParameter("codigo");
            String prazoStr = req.getParameter("prazo");
            String maxQuestoesStr = req.getParameter("maxQuestoes");

            // 2. Pega o professor da sessão (Quem está logado)
            HttpSession session = req.getSession();
            Professor professor = (Professor) session.getAttribute("professorLogado");


            if (professor == null) {
                resp.sendRedirect("login.jsp");
                return;
            }

            // 3. Monta o objeto Avaliacao
            Avaliacao avaliacao = new Avaliacao();
            avaliacao.setCodAvaliacao(codigo);
            avaliacao.setDataPublicacao(LocalDate.now());
            avaliacao.setPrazo(LocalDate.parse(prazoStr));

            if (maxQuestoesStr != null && !maxQuestoesStr.isEmpty()) {
                avaliacao.setMaxQuestoes(Integer.parseInt(maxQuestoesStr));
            }

            Disciplina d = new Disciplina();
            d.setIdDisciplina(Integer.parseInt(idDisciplinaStr));
            avaliacao.setDisciplina(d);

            avaliacao.setProfessorCriador(professor);

            // 4. Salva no Banco
            AvaliacaoDAO dao = new AvaliacaoDAO();
            dao.salvar(avaliacao);

            // 5. Redireciona para a tela de montar prova
            resp.sendRedirect("montarProva?idAvaliacao=" + avaliacao.getIdAvaliacao());

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("nova_avaliacao.jsp?erro=true");
        }
    }
}