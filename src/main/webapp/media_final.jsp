<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.TrabalhoDAO" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.math.BigDecimal" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // 1. Instancia o DAO
    TrabalhoDAO trabalhoDAO = new TrabalhoDAO();

    // 2. Chama o método de relatório (Média Global de todas as APs)
    Map<String, BigDecimal> medias = trabalhoDAO.getOverallFinalAverage();

    // 3. Guarda o resultado
    request.setAttribute("mediasGerais", medias);
%>

<html>
<head>
    <title>Média Final Global</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">

    <h1>Média Final Global (Todas as Avaliações)</h1>

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
                            <span style='color: #dc3545; font-weight: bold;'>REPROVADO</span>
                        </c:when>
                        <c:when test="${entry.value < 6.0}">
                            <span style='color: #ffc107; font-weight: bold;'>EXAME</span>
                        </c:when>
                        <c:otherwise>
                            <span style='color: #28a745; font-weight: bold;'>APROVADO</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>

    <br/>
    <a href="index.jsp">Voltar para o Dashboard</a>

</div> </body>
</html>