package org.example.controller;

import org.example.dao.UsuarioDAO;
import org.example.dao.DisciplinaDAO;
import org.example.model.Professor;
import org.example.model.Disciplina;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/cadastrarProfessor")
public class CadastrarProfessorServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Pegar os dados do formulário
            String pnome = request.getParameter("pnome");
            String snome = request.getParameter("snome");
            String email = request.getParameter("email");
            String senha = request.getParameter("senha");
            String nomeDisciplina = request.getParameter("nomeDisciplina");

            // 2. Criar o objeto Professor
            Professor novoProfessor = new Professor();
            novoProfessor.setPnome(pnome);
            novoProfessor.setSnome(snome);
            novoProfessor.setEmail(email);
            novoProfessor.setSenha(senha);

            // 3. Salvar o Professor
            usuarioDAO.salvar(novoProfessor);

            // 4. Criar o objeto Disciplina, usando o ID que acabamos de gerar
            Disciplina novaDisciplina = new Disciplina();
            novaDisciplina.setNome(nomeDisciplina);
            novaDisciplina.setProfessor(novoProfessor); // ID do Professor já está no objeto

            // 5. Salvar a Disciplina
            disciplinaDAO.salvar(novaDisciplina);

            // 6. Redirecionar para o dashboard
            response.sendRedirect("index.jsp");

        } catch (Exception e) {
            // Trata erro de email duplicado ou falha na FK
            throw new ServletException("Erro ao cadastrar Professor e Disciplina. O email pode já estar em uso.", e);
        }
    }
}