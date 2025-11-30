<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.TrabalhoDAO" %>
<%@ page import="org.example.model.RankingDTO" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // 1. Pega o ID da avaliação que veio da URL
    int idAvaliacao = Integer.parseInt(request.getParameter("id"));

    // 2. Instancia o DAO
    TrabalhoDAO trabalhoDAO = new TrabalhoDAO();

    // 3. Chama o método do relatório usando o ID dinâmico
    List<RankingDTO> ranking = trabalhoDAO.getRankingAvaliacao(idAvaliacao);

    // 4. Guarda o resultado
    request.setAttribute("ranking", ranking);
    request.setAttribute("idAvaliacao", idAvaliacao);
%>

<html>
<head>
    <title>Ranking da Avaliação #${idAvaliacao}</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">

    <h1>Ranking da Avaliação (ID: ${idAvaliacao})</h1>

    <table>
        <tr>
            <th>Posição</th>
            <th>Aluno</th>
            <th>Nota Final</th>
        </tr>

        <c:forEach var="item" items="${ranking}">
            <tr>
                <td style="font-weight: bold; color: #007bff;">${item.rank}º</td>
                <td><c:out value="${item.nomeEstudante}" /></td>
                <td><c:out value="${item.notaFinal}" /></td>
            </tr>
        </c:forEach>
    </table>

    <br/>
    <a href="avaliacoes.jsp">Voltar para Avaliações</a>

</div> </body>
</html>