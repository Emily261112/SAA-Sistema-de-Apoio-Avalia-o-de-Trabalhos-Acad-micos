package org.example.controller;

import org.example.dao.UsuarioDAO;
import org.example.model.Professor;
import org.example.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        try {
            Usuario usuario = usuarioDAO.findByEmail(email);

            // 1. Verifica se o usuário existe, se é um Professor E se a senha confere
            if (usuario != null && usuario instanceof Professor && usuario.getSenha().equals(senha)) {

                // 2. Login bem-sucedido
                HttpSession session = request.getSession(true);
                session.setAttribute("usuarioLogado", usuario); // Guarda o objeto Professor na sessão

                // 3. Redireciona para o Dashboard principal
                response.sendRedirect("index.jsp");

            } else {
                // 4. Falha no Login: Retorna para a página de login com mensagem de erro
                request.setAttribute("erroLogin", "Email ou senha inválidos, ou este usuário não é um Professor.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            throw new ServletException("Erro durante a tentativa de login", e);
        }
    }
}