<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.TrabalhoDAO" %>
<%@ page import="org.example.model.Trabalho" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // 1. Pega o ID da avaliação que veio pela URL
    int idAvaliacao = Integer.parseInt(request.getParameter("id"));

    // 2. Busca no banco os trabalhos SOMENTE dessa avaliação
    TrabalhoDAO dao = new TrabalhoDAO();
    List<Trabalho> trabalhos = dao.findTrabalhosByAvaliacao(idAvaliacao);

    request.setAttribute("trabalhos", trabalhos);
%>

<html>
<head>
    <title>Trabalhos Enviados</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">

    <h1>Trabalhos Enviados (Avaliação ID: ${param.id})</h1>

    <table>
        <tr>
            <th>Aluno</th>
            <th>Arquivo</th>
            <th>Data de Envio</th>
            <th>Status</th>
            <th>Ação</th>
        </tr>

        <c:forEach var="t" items="${trabalhos}">
            <tr>
                <td><c:out value="${t.estudante.pnome} ${t.estudante.snome}" /></td>
                <td><c:out value="${t.arquivoUrl}" /></td>
                <td><c:out value="${t.dataEnvio}" /></td>
                <td><c:out value="${t.status}" /></td>
                <td>
                    <a href="lancar_nota.jsp?id_trabalho=${t.idTrabalho}" class="acao-editar">Lançar Nota</a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <br/>
    <a href="avaliacoes.jsp">Voltar para Avaliações</a>

</div> </body>
</html>