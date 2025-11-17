<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.UsuarioDAO" %>
<%@ page import="org.example.dao.DisciplinaDAO" %>
<%@ page import="org.example.model.Professor" %>
<%@ page import="org.example.model.Disciplina" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // NOVO: Pega o objeto Professor Logado da sessão
    Professor professorLogado = (Professor) session.getAttribute("usuarioLogado");
    int idProfessor = professorLogado.getIdUsuario(); // ID do professor logado

    // 1. Instancia os DAOs
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

    // 2. CORREÇÃO: Busca APENAS as disciplinas que o professor ministra (adm_prof_id = idProfessor)
    List<Disciplina> disciplinas = disciplinaDAO.findDisciplinasByProfessor(idProfessor);

    // Armazenamos para o formulário usar
    request.setAttribute("disciplinas", disciplinas);
%>

<html>
<head>
    <title>Nova Avaliação</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        form { width: 50%; border: 1px solid #ddd; padding: 20px; border-radius: 8px; }
        div { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="date"], select {
            width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;
        }
        button { background-color: #007bff; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; }
        a { color: #007bff; text-decoration: none; }
    </style>
</head>
<body>

<h1>Cadastrar Nova Avaliação</h1>

<form action="criarAvaliacao" method="POST">
    <div>
        <label for="disciplina">Disciplina:</label>
        <select id="disciplina" name="idDisciplina">
            <c:forEach var="d" items="${disciplinas}">
                <option value="${d.idDisciplina}">
                    <c:out value="${d.nome}" />
                </option>
            </c:forEach>
        </select>
    </div>

    <div>
        <label for="codAvaliacao">Código da Avaliação (ex: AP2):</label>
        <input type="text" id="codAvaliacao" name="codAvaliacao" required>
    </div>

    <div>
        <label for="prazo">Prazo de Entrega:</label>
        <input type="date" id="prazo" name="prazo" required>
    </div>

    <button type="submit">Salvar Avaliação</button>
</form>

<br/>
<a href="index.jsp">Voltar para o Dashboard</a>

</body>
</html>