<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.TrabalhoDAO" %>
<%@ page import="org.example.model.RankingDTO" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // 1. CORREÇÃO: Pega o ID da avaliação que veio da URL (ex: ?id=1, ?id=2)
    int idAvaliacao = Integer.parseInt(request.getParameter("id"));

    // 2. Instancia o DAO
    TrabalhoDAO trabalhoDAO = new TrabalhoDAO();

    // 3. Chama o método do relatório (o SQL com RANK()) usando o ID dinâmico
    List<RankingDTO> ranking = trabalhoDAO.getRankingAvaliacao(idAvaliacao);

    // 4. Guarda o resultado para o JSP usar
    request.setAttribute("ranking", ranking);
    request.setAttribute("idAvaliacao", idAvaliacao); // Guarda o ID para o título
%>

<html>
<head>
    <title>Ranking da Avaliação #${idAvaliacao}</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        table { border-collapse: collapse; width: 60%; }
        th, td { border: 1px solid #ddd; padding: 8px; }
        th { background-color: #f2f2f2; text-align: left; }
        a { color: #007bff; text-decoration: none; }
    </style>
</head>
<body>

<h1>Ranking da Avaliação (ID: ${idAvaliacao})</h1>

<table>
    <tr>
        <th>Posição</th>
        <th>Aluno</th>
        <th>Nota Final</th>
    </tr>

    <c:forEach var="item" items="${ranking}">
        <tr>
            <td><c:out value="${item.rank}" /></td>
            <td><c:out value="${item.nomeEstudante}" /></td>
            <td><c:out value="${item.notaFinal}" /></td>
        </tr>
    </c:forEach>
</table>

<br/>
<a href="avaliacoes.jsp">Voltar para Avaliações</a>

</body>
</html>