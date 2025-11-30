<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.AvaliacaoDAO" %>
<%@ page import="org.example.model.Avaliacao" %>
<%@ page import="org.example.model.Professor" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Verifica login
    Object usuario = session.getAttribute("usuarioLogado");
    if (usuario == null || !(usuario instanceof Professor)) {
        response.sendRedirect("login.jsp");
        return;
    }
    Professor professorLogado = (Professor) usuario;
    int idProfessor = professorLogado.getIdUsuario();

    // Busca avaliações
    AvaliacaoDAO dao = new AvaliacaoDAO();
    List<Avaliacao> avaliacoes = dao.findAvaliacoesByProfessor(idProfessor);
    request.setAttribute("avaliacoes", avaliacoes);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Lista de Avaliações</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">

    <h1>Avaliações Cadastradas</h1>

    <table>
        <thead>
        <tr>
            <th>Código</th>
            <th>Disciplina</th>
            <th>Professor</th>
            <th>Prazo Final</th>
            <th>Ação</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="av" items="${avaliacoes}">
            <tr>
                <td><c:out value="${av.codAvaliacao}" /></td>
                <td><c:out value="${av.disciplina.nome}" /></td>
                <td><c:out value="${av.professorCriador.pnome}" /></td>
                <td><c:out value="${av.prazo}" /></td>
                <td>
                    <a href="lancar_nota.jsp?id=${av.idAvaliacao}">Lançar Notas</a> |

                    <a href="deletarAvaliacao?id=${av.idAvaliacao}"
                       onclick="return confirm('Tem certeza que deseja excluir a avaliação ${av.codAvaliacao}?');"
                       class="excluir">Excluir</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <br/>
    <a href="index.jsp">Voltar para o Dashboard</a>

</div>
</body>
</html>