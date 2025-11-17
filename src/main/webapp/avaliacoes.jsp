<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.AvaliacaoDAO" %>
<%@ page import="org.example.model.Avaliacao" %>
<%@ page import="org.example.model.Professor" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // 1. Pega o objeto Professor Logado da sessão
    Professor professorLogado = (Professor) session.getAttribute("usuarioLogado");
    int idProfessor = professorLogado.getIdUsuario();

    // 2. BUSCA AS AVALIAÇÕES SOMENTE DESTE PROFESSOR (Método Filtrado)
    AvaliacaoDAO dao = new AvaliacaoDAO();
    List<Avaliacao> avaliacoes = dao.findAvaliacoesByProfessor(idProfessor);

    request.setAttribute("avaliacoes", avaliacoes);
%>

<html>
<head>
    <title>Lista de Avaliações</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        table { border-collapse: collapse; width: 60%; }
        th, td { border: 1px solid #ddd; padding: 8px; }
        th { background-color: #f2f2f2; text-align: left; }
        a { color: #007bff; text-decoration: none; }
        /* Estilos para os links de Ação */
        .excluir { color: red; }
        .ranking { color: green; }
    </style>
</head>
<body>

<h1>Avaliações Cadastradas</h1>

<table>
    <tr>
        <th>Código (AP1, AP2...)</th>
        <th>Disciplina</th>
        <th>Professor</th>
        <th>Prazo Final</th>
        <th>Ação</th>
    </tr>

    <c:forEach var="av" items="${avaliacoes}">
        <tr>
            <td><c:out value="${av.codAvaliacao}" /></td>
            <td><c:out value="${av.disciplina.nome}" /></td>
            <td><c:out value="${av.professorCriador.pnome}" /></td>
            <td><c:out value="${av.prazo}" /></td>
            <td>
                <a href="trabalhos.jsp?id=${av.idAvaliacao}">Ver Envios</a> |

                <a href="ranking.jsp?id=${av.idAvaliacao}"
                   class="ranking">Ver Ranking</a> |
                <a href="deletarAvaliacao?id=${av.idAvaliacao}"
                   onclick="return confirm('Tem certeza que deseja excluir a avaliação ${av.codAvaliacao} e todos os seus dados (trabalhos, notas, critérios)?');"
                   class="excluir">Excluir</a>
            </td>
        </tr>
    </c:forEach>
</table>

<br/>
<a href="index.jsp">Voltar para o Dashboard</a>

</body>
</html>