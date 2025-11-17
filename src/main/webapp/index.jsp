<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.DisciplinaDAO" %>
<%@ page import="org.example.dao.MatriculaDAO" %>
<%@ page import="org.example.model.Estudante" %>
<%@ page import="org.example.model.Professor" %>
<%@ page import="org.example.model.Disciplina" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
    ======================================
    🔒 GUARDA DE SEGURANÇA (AUTENTICAÇÃO)
    ======================================
--%>
<%
    // Verifica se há um usuário na sessão E se ele é um Professor
    Object usuario = session.getAttribute("usuarioLogado");
    if (usuario == null || !(usuario instanceof Professor)) {
        response.sendRedirect("login.jsp");
        return; // Pára a execução do restante da página
    }
    // Armazena o objeto Professor na página para ser exibido
    Professor professor = (Professor) usuario;
    request.setAttribute("professorLogado", professor);
%>

<html>
<head>
    <title>Avaliador de Trabalhos</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; display: inline-block; }
        .header-top { display: flex; justify-content: space-between; align-items: center; }
        table { border-collapse: collapse; width: 50%; margin-top: 10px; }
        th, td { border: 1px solid #ddd; padding: 8px; }
        th { background-color: #f2f2f2; text-align: left; }
        a { color: #007bff; text-decoration: none; font-size: 1.1em; }
    </style>
</head>
<body>

<div class="header-top">
    <h1>Dashboard de Disciplinas</h1>
    <p>Logado como: <b><c:out value="${professorLogado.pnome} ${professorLogado.snome}" /></b> | <a href="logout">Logout</a></p>
</div>

<a href="nova_avaliacao.jsp">[+] Cadastrar Nova Avaliação</a>
<br/><br/>

<%
    // 1. Instancia os DAOs
    DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    MatriculaDAO matriculaDAO = new MatriculaDAO();

    // 2. BUSCA AS DISCIPLINAS FILTRADAS PELO PROFESSOR LOGADO
    int idProfessorLogado = professor.getIdUsuario(); // Pega o ID da sessão!
    List<Disciplina> disciplinasDoProfessor = disciplinaDAO.findDisciplinasByProfessor(idProfessorLogado);

    // 3. Armazena a lista filtrada para o JSP usar
    request.setAttribute("disciplinasDoProfessor", disciplinasDoProfessor);

    // 4. CHAMA O RELATÓRIO DE NOMES (usando o ID da primeira disciplina do professor para exibição inicial)
    List<Estudante> listaAlunos = List.of(); // Lista vazia por padrão
    if (!disciplinasDoProfessor.isEmpty()) {
        // Pega a primeira disciplina da lista do professor para exibir os alunos
        int idDisciplinaBase = disciplinasDoProfessor.get(0).getIdDisciplina();
        listaAlunos = matriculaDAO.findEstudantesByDisciplina(idDisciplinaBase);
    }
    request.setAttribute("listaAlunos", listaAlunos);
%>

<h2>Relatório: Alunos por Disciplina</h2>

<table>
    <tr>
        <th>Disciplina</th>
        <th>Total de Alunos</th>
    </tr>

    <c:forEach var="disciplina" items="${disciplinasDoProfessor}">
        <tr>
            <td><c:out value="${disciplina.nome}" /></td>
            <td>
                <%
                    // Chama a contagem de alunos para CADA disciplina do professor
                    Disciplina currentDisciplina = (Disciplina) pageContext.getAttribute("disciplina");

                    // Busca o total de alunos (usando o método findEstudantesByDisciplina e pegando o tamanho da lista)
                    int totalAlunos = matriculaDAO.findEstudantesByDisciplina(currentDisciplina.getIdDisciplina()).size();
                    out.print(totalAlunos);
                %>
            </td>
        </tr>
    </c:forEach>
</table>

<br/>
<hr/>
<h3>Ver Relatórios Detalhados</h3>

<a href="avaliacoes.jsp">Lançar Notas de Avaliações</a>

<br/><br/>
<a href="media_final.jsp" style="color: blue;">Ver Média Final Global</a>
<br/><br/>
<h3>Alunos Matriculados (Nomes)</h3>
<ul>
    <c:forEach var="aluno" items="${listaAlunos}">
        <li><c:out value="${aluno.pnome} ${aluno.snome} (ID: ${aluno.idUsuario})" /></li>
    </c:forEach>
</ul>
</body>
</html>