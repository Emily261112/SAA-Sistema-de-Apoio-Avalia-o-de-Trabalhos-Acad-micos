<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.CriterioDAO" %>
<%@ page import="org.example.dao.TrabalhoDAO" %>
<%@ page import="org.example.model.Criterio" %>
<%@ page import="org.example.model.Trabalho" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // 1. Pega o ID do Trabalho da URL (ex: 7)
    int idTrabalho = Integer.parseInt(request.getParameter("id_trabalho"));

    CriterioDAO criterioDAO = new CriterioDAO();

    // 2. CORREÇÃO: Busca os critérios dinamicamente
    //    Usando o novo método que busca pelo ID do Trabalho.
    List<Criterio> criterios = criterioDAO.findCriteriosByTrabalhoId(idTrabalho);

    request.setAttribute("criterios", criterios);
    request.setAttribute("idTrabalho", idTrabalho);
%>

<html>
<head>
    <title>Lançar Notas</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; } h1 { color: #333; }
        form { width: 50%; border: 1px solid #ddd; padding: 20px; border-radius: 8px; }
        div { margin-bottom: 15px; } label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="number"], textarea {
            width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;
        }
        button { background-color: #28a745; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; }
        a { color: #007bff; text-decoration: none; }
    </style>
</head>
<body>

<h1>Lançar Notas (Trabalho ID: ${idTrabalho})</h1>

<form action="salvarNota" method="POST">
    <input type="hidden" name="idTrabalho" value="${idTrabalho}">

    <c:forEach var="c" items="${criterios}">
        <hr>
        <h3><c:out value="${c.nome}" /> (Peso: <c:out value="${c.peso}" />)</h3>

        <input type="hidden" name="idCriterio" value="${c.idCriterio}">

        <div>
            <label for="nota_${c.idCriterio}">Nota:</label>
            <input type="number" step="0.1" max="${c.peso}" min="0"
                   id="nota_${c.idCriterio}" name="nota_${c.idCriterio}" required>
        </div>
        <div>
            <label for="obs_${c.idCriterio}">Observação:</label>
            <textarea id="obs_${c.idCriterio}" name="obs_${c.idCriterio}" rows="2"></textarea>
        </div>
    </c:forEach>

    <br>
    <button type="submit">Salvar Notas</button>
</form>

<br/>
<a href="avaliacoes.jsp">Voltar</a>

</body>
</html>