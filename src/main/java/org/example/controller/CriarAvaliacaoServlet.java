package org.example.controller;

import org.example.dao.AvaliacaoDAO;
import org.example.dao.CriterioDAO; // <--- NOVO IMPORT
import org.example.model.Avaliacao;
import org.example.model.Disciplina;
import org.example.model.Professor;
import org.example.model.Criterio; // <--- NOVO IMPORT
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.math.BigDecimal; // <--- NOVO IMPORT

@WebServlet("/criarAvaliacao")
public class CriarAvaliacaoServlet extends HttpServlet {

    private AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();
    private CriterioDAO criterioDAO = new CriterioDAO(); // <--- INSTANCIA O CRITERIO DAO

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Professor professorLogado = (Professor) session.getAttribute("usuarioLogado");

        if (professorLogado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 1. Pega dados do formulário
            int idDisciplina = Integer.parseInt(request.getParameter("idDisciplina"));
            String codAvaliacao = request.getParameter("codAvaliacao");
            LocalDate prazo = LocalDate.parse(request.getParameter("prazo"));

            // 2. Monta o objeto Avaliacao
            Avaliacao novaAvaliacao = new Avaliacao();
            novaAvaliacao.setDisciplina(new Disciplina(idDisciplina)); // Stub
            novaAvaliacao.setProfessorCriador(new Professor(professorLogado.getIdUsuario())); // Stub
            novaAvaliacao.setCodAvaliacao(codAvaliacao);
            novaAvaliacao.setPrazo(prazo);
            novaAvaliacao.setDataPublicacao(LocalDate.now());

            // 3. SALVA A AVALIAÇÃO e obtem o novo ID
            avaliacaoDAO.salvar(novaAvaliacao); // O ID da nova Avaliacao agora está no objeto 'novaAvaliacao'

            // 4. NOVO PASSO: CRIA E INSERE OS CRITÉRIOS PADRÃO (Transação Única)

            // 4a. Critério 1: Originalidade (Peso 4.0)
            Criterio c1 = new Criterio();
            c1.setAvaliacao(novaAvaliacao); // Usa o objeto com o novo ID
            c1.setNome("Originalidade");
            c1.setDescricao("Avalia a originalidade e inovação do projeto.");
            c1.setPeso(new BigDecimal("4.0"));
            criterioDAO.salvar(c1);

            // 4b. Critério 2: Coesão Textual (Peso 6.0)
            Criterio c2 = new Criterio();
            c2.setAvaliacao(novaAvaliacao);
            c2.setNome("Coesão Textual");
            c2.setDescricao("Avalia a clareza e estrutura do relatório.");
            c2.setPeso(new BigDecimal("6.0"));
            criterioDAO.salvar(c2);

            // 5. Redirecionar
            response.sendRedirect("avaliacoes.jsp");

        } catch (Exception e) {
            throw new ServletException("Erro ao salvar avaliação. Causa: " + e.getMessage(), e);
        }
    }

    // Construtores Stubs (Pode ser que precise adicionar estes construtores nas classes Model se não existirem)
}