<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.DisciplinaDAO" %>
<%@ page import="org.example.dao.MatriculaDAO" %>
<%@ page import="org.example.model.Professor" %>
<%@ page import="org.example.model.Disciplina" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Lógica de Segurança e Dados
    Object usuario = session.getAttribute("usuarioLogado");
    if (usuario == null || !(usuario instanceof Professor)) {
        response.sendRedirect("login.jsp");
        return;
    }
    Professor professor = (Professor) usuario;
    request.setAttribute("professorLogado", professor);

    DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    MatriculaDAO matriculaDAO = new MatriculaDAO();

    int idProfessorLogado = professor.getIdUsuario();
    List<Disciplina> disciplinasDoProfessor = disciplinaDAO.findDisciplinasByProfessor(idProfessorLogado);
    request.setAttribute("disciplinasDoProfessor", disciplinasDoProfessor);
%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <title>Dashboard | Avaliador Acadêmico</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <style>
        /* Reset e Estilos Base */
        body {
            font-family: 'Inter', sans-serif;
            background-color: #f3f4f6; /* Fundo cinza claro moderno */
            margin: 0;
            padding: 0;
            color: #1f2937;
        }

        /* Navbar Profissional */
        .navbar {
            background-color: #1f2937; /* Azul-acinzentado escuro */
            color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
        }
        .nav-brand { font-size: 1.25rem; font-weight: 700; display: flex; align-items: center; gap: 12px; }
        .nav-user { display: flex; align-items: center; gap: 20px; font-size: 0.95rem; }

        .btn-logout {
            background-color: #dc2626; color: white; padding: 8px 16px; border-radius: 6px;
            text-decoration: none; font-weight: 600; font-size: 0.85rem; transition: background 0.2s;
        }
        .btn-logout:hover { background-color: #b91c1c; }

        .container {
            max-width: 1100px; /* Um pouco mais estreito para focar o conteúdo */
            margin: 50px auto;
            padding: 0 25px;
        }

        /* MENU DE AÇÕES (Botões Grandes com Ícones) */
        .section-title {
            color: #4b5563; margin-bottom: 20px; font-size: 1.1rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px;
        }

        .actions-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); /* Responsivo */
            gap: 25px;
            margin-bottom: 50px;
        }

        .action-btn {
            background: white;
            padding: 30px 20px; /* Mais espaçamento interno */
            border-radius: 16px; /* Bordas mais arredondadas */
            text-align: center;
            text-decoration: none;
            color: #374151;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03);
            transition: all 0.3s ease;
            border: 2px solid transparent; /* Borda transparente para o hover não pular */
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 15px;
        }

        .action-btn i {
            font-size: 2.5rem; /* Ícones grandes */
            color: #4b5563; /* Cinza base */
            transition: color 0.3s, transform 0.3s;
        }

        .action-btn span {
            font-weight: 700;
            font-size: 1.1em;
        }

        /* Efeito Hover nos Botões */
        .action-btn:hover {
            transform: translateY(-5px);
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
            border-color: #3b82f6; /* Borda azul ao passar o mouse */
        }
        .action-btn:hover i {
            color: #3b82f6; /* Ícone fica azul */
            transform: scale(1.1); /* Ícone cresce um pouco */
        }

        /* TABELA MODERNA */
        .table-card {
            background: white;
            border-radius: 16px;
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            border: 1px solid #e5e7eb;
        }
        .table-header {
            padding: 25px;
            border-bottom: 1px solid #e5e7eb;
            background-color: #fff;
            display: flex; align-items: center; gap: 12px;
        }
        .table-header h2 { margin: 0; font-size: 1.35rem; color: #111; font-weight: 700; }

        table { width: 100%; border-collapse: collapse; }
        thead { background-color: #f9fafb; }
        th {
            text-align: left; padding: 18px 25px; font-size: 0.8rem;
            text-transform: uppercase; letter-spacing: 0.05em; color: #6b7280; font-weight: 700;
        }
        td {
            padding: 20px 25px; border-bottom: 1px solid #f3f4f6;
            color: #374151; font-weight: 600; font-size: 1rem;
        }
        tr:last-child td { border-bottom: none; }
        tr:hover { background-color: #f0f9ff; } /* Azul bem clarinho ao passar o mouse na linha */

        /* Badges */
        .badge-number {
            background-color: #e0e7ff; color: #4338ca;
            padding: 6px 14px; border-radius: 99px; font-size: 0.9rem; font-weight: 700;
        }
        .status-text { color: #059669; font-weight: 700; font-size: 0.9rem; display: flex; align-items: center; gap: 6px;}
        .status-dot { height: 10px; width: 10px; background-color: #10b981; border-radius: 50%; display: inline-block; }
    </style>
</head>
<body>

<nav class="navbar">
    <div class="nav-brand">
        <i class="fas fa-graduation-cap" style="font-size: 1.5rem;"></i>
        <span>Avaliador Acadêmico</span>
    </div>
    <div class="nav-user">
        <span>Olá, <strong>${professorLogado.pnome}</strong></span>
        <a href="logout" class="btn-logout">Sair</a>
    </div>
</nav>

<div class="container">

    <div class="section-title">Acesso Rápido</div>

    <div class="actions-grid">
        <a href="nova_avaliacao.jsp" class="action-btn">
            <i class="fas fa-plus-circle"></i>
            <span>Nova Avaliação</span>
        </a>

        <a href="avaliacoes.jsp" class="action-btn">
            <i class="fas fa-edit"></i>
            <span>Lançar Notas</span>
        </a>

        <a href="relatorios.jsp" class="action-btn">
            <i class="fas fa-chart-pie"></i>
            <span>Relatórios</span>
        </a>

        <a href="alunos.jsp" class="action-btn">
            <i class="fas fa-user-graduate"></i>
            <span>Alunos Matriculados</span>
        </a>
    </div>


    <div class="table-card">
        <div class="table-header">
            <i class="fas fa-list-alt" style="color: #3b82f6; font-size: 1.4rem;"></i>
            <h2>Resumo das Disciplinas</h2>
        </div>
        <table>
            <thead>
            <tr>
                <th>Nome da Disciplina</th>
                <th>Qtd. Alunos</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="disciplina" items="${disciplinasDoProfessor}">
                <tr>
                    <td style="color: #111;">
                        <c:out value="${disciplina.nome}" />
                    </td>
                    <td>
                        <%
                            Disciplina currentDisciplina = (Disciplina) pageContext.getAttribute("disciplina");
                            int totalAlunos = matriculaDAO.findEstudantesByDisciplina(currentDisciplina.getIdDisciplina()).size();
                        %>
                        <span class="badge-number"><%= totalAlunos %> alunos</span>
                    </td>
                    <td>
                            <span class="status-text">
                                <span class="status-dot"></span> Ativa
                            </span>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <c:if test="${empty disciplinasDoProfessor}">
            <div style="padding: 50px; text-align: center; color: #9ca3af; display: flex; flex-direction: column; align-items: center; gap: 15px;">
                <i class="far fa-folder-open" style="font-size: 3rem; opacity: 0.5;"></i>
                <p style="font-size: 1.1rem;">Nenhuma disciplina cadastrada ainda.</p>
            </div>
        </c:if>
    </div>

</div>

</body>
</html>