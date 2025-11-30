<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.UsuarioDAO" %>
<%@ page import="org.example.dao.DisciplinaDAO" %>
<%@ page import="org.example.model.Professor" %>
<%@ page import="org.example.model.Disciplina" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // 1. Pega o objeto Professor Logado da sessão
    Object usuario = session.getAttribute("usuarioLogado");

    // Pequena proteção caso a sessão tenha expirado
    if (usuario == null || !(usuario instanceof Professor)) {
        response.sendRedirect("login.jsp");
        return;
    }

    Professor professorLogado = (Professor) usuario;

    // Guardamos o professor na sessão com o nome que o Servlet espera, por garantia
    session.setAttribute("professorLogado", professorLogado);

    int idProfessor = professorLogado.getIdUsuario();

    // 2. Busca as disciplinas
    DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    List<Disciplina> disciplinas = disciplinaDAO.findDisciplinasByProfessor(idProfessor);
    request.setAttribute("disciplinas", disciplinas);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Nova Avaliação</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input, select { padding: 8px; width: 100%; max-width: 400px; }
        button { margin-top: 20px; padding: 10px 20px; cursor: pointer; }
    </style>
</head>
<body>
<div class="container">

    <h1>Cadastrar Nova Avaliação</h1>

    <form action="cadastrarAvaliacao" method="POST">

        <div class="form-group">
            <label for="disciplina">Disciplina:</label>
            <select id="disciplina" name="disciplina" required>
                <c:forEach var="d" items="${disciplinas}">
                    <option value="${d.idDisciplina}">
                        <c:out value="${d.nome}" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="codigo">Código da Avaliação (ex: AP2):</label>
            <input type="text" id="codigo" name="codigo" placeholder="Digite o código..." required>
        </div>

        <div class="form-group">
            <label for="maxQuestoes">Quantidade Máxima de Questões:</label>
            <input type="number" id="maxQuestoes" name="maxQuestoes" min="1" value="10" required>
        </div>

        <div class="form-group">
            <label for="prazo">Prazo de Entrega:</label>
            <input type="date" id="prazo" name="prazo" required>
        </div>

        <button type="submit">Salvar e Montar Prova</button>
    </form>

    <br/>
    <a href="index.jsp">Voltar para o Dashboard</a>

</div>
</body>
</html>