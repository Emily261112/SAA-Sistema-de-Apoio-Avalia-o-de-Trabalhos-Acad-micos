<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.TrabalhoDAO" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.math.BigDecimal" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // 1. Instancia o DAO
    TrabalhoDAO trabalhoDAO = new TrabalhoDAO();

    // 2. Chama o método de relatório
    Map<String, BigDecimal> medias = trabalhoDAO.getOverallFinalAverage();

    // 3. Guarda o resultado para o JSP usar
    request.setAttribute("mediasGerais", medias);
%>

<html>
<head>
    <title>Média Final Global</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; } h1 { color: #333; }
        table { border-collapse: collapse; width: 60%; margin-top: 15px; }
        th, td { border: 1px solid #ddd; padding: 10px; } th { background-color: #f2f2f2; text-align: left; }
    </style>
</head>
<body>

<h1>Média Final Global (Todas as Avaliações)</h1>
<a href="index.jsp">Voltar para o Dashboard</a>
<br/>

<table>
    <tr>
        <th>Aluno</th>
        <th>Média Geral</th>
        <th>Situação Final</th>
    </tr>

    <c:forEach var="entry" items="${mediasGerais}">
        <tr>
            <td><c:out value="${entry.key}" /></td>
            <td><c:out value="${entry.value}" /></td>
            <td>
                <c:choose>
                    <c:when test="${entry.value < 3.0}">
                        <span style='color: red; font-weight: bold;'>REPROVADO</span>
                    </c:when>
                    <c:when test="${entry.value < 6.0}">
                        <span style='color: orange; font-weight: bold;'>EXAME</span>
                    </c:when>
                    <c:otherwise>
                        <span style='color: green; font-weight: bold;'>APROVADO</span>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>