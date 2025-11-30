<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.CriterioDAO" %>
<%@ page import="org.example.model.Criterio" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // 1. Pega o ID da Prova que acabamos de criar
    String idParam = request.getParameter("idAvaliacao");
    if (idParam == null) { response.sendRedirect("index.jsp"); return; }
    int idAvaliacao = Integer.parseInt(idParam);

    // 2. Lista as questões que JÁ foram adicionadas nesta prova
    CriterioDAO dao = new CriterioDAO();
    // Reutilizamos o método que já existe!
    List<Criterio> questoesJaCriadas = dao.findCriteriosByAvaliacao(idAvaliacao);

    request.setAttribute("questoes", questoesJaCriadas);
    request.setAttribute("idAvaliacao", idAvaliacao);
%>

<html>
<head>
    <title>Montar Prova</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">
    <h1>Montar Prova (Adicionar Questões)</h1>

    <h3>Questões na Prova:</h3>
    <table style="margin-bottom: 30px;">
        <tr>
            <th>Título da Questão</th>
            <th>Enunciado / Descrição</th>
            <th>Peso (Pontos)</th>
        </tr>
        <c:forEach var="q" items="${questoes}">
            <tr>
                <td>${q.nome}</td>
                <td>${q.descricao}</td>
                <td>${q.peso}</td>
            </tr>
        </c:forEach>
    </table>

    <hr>

    <h3>Adicionar Nova Questão</h3>
    <form action="adicionarQuestao" method="POST">
        <input type="hidden" name="idAvaliacao" value="${idAvaliacao}">

        <div>
            <label>Título (ex: Questão 1)</label>
            <input type="text" name="titulo" required placeholder="Questão 1">
        </div>
        <div>
            <label>Enunciado da Pergunta</label>
            <textarea name="enunciado" rows="3" required placeholder="Digite a pergunta aqui..."></textarea>
        </div>
        <div>
            <label>Quanto vale? (Peso)</label>
            <input type="number" name="peso" step="0.1" required>
        </div>

        <button type="submit" class="salvar">Adicionar na Prova</button>
    </form>

    <br>
    <a href="avaliacoes.jsp" class="btn-link">Finalizar e Voltar</a>
</div>
</body>
</html>