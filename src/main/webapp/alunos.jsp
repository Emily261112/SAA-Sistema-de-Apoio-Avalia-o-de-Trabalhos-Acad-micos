<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.MatriculaDAO" %>
<%@ page import="org.example.model.Estudante" %>
<%@ page import="org.example.model.Professor" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Verifica Login
    Object usuario = session.getAttribute("usuarioLogado");
    if (usuario == null || !(usuario instanceof Professor)) {
        response.sendRedirect("login.jsp");
        return;
    }
    Professor professor = (Professor) usuario;

    // Busca os alunos matriculados na disciplina do professor (Assumindo ID 1 para teste ou lógica dinâmica)
    // Se você tiver a lógica de selecionar disciplina antes, ajuste aqui.
    // Para simplificar e mostrar todos do professor na disciplina 1 (Algoritmos):
    MatriculaDAO matriculaDAO = new MatriculaDAO();
    List<Estudante> alunos = matriculaDAO.findEstudantesByDisciplina(1);

    request.setAttribute("alunos", alunos);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Alunos Matriculados</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <style>
        .student-list {
            list-style: none;
            padding: 0;
            margin-top: 20px;
        }

        .student-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
            background-color: #fff;
            padding: 15px 20px;
            border-bottom: 1px solid #eee;
            transition: background-color 0.2s;
        }

        .student-item:first-child {
            border-top-left-radius: 8px;
            border-top-right-radius: 8px;
        }

        .student-item:last-child {
            border-bottom: none;
            border-bottom-left-radius: 8px;
            border-bottom-right-radius: 8px;
        }

        .student-item:hover {
            background-color: #f9fbff; /* Leve azul ao passar o mouse */
        }

        .student-info {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .avatar-circle {
            width: 40px;
            height: 40px;
            background-color: #e9ecef;
            color: #555;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 18px;
        }

        .student-name {
            font-weight: 600;
            color: #333;
            font-size: 1.05em;
        }

        .student-email {
            font-size: 0.85em;
            color: #777;
            font-weight: normal;
        }

        .status-badge {
            background-color: #d4edda;
            color: #155724;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 0.85em;
            font-weight: bold;
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .card-container {
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
            border: 1px solid #e0e0e0;
        }
    </style>
</head>
<body>
<div class="container">

    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
        <h1><i class="fas fa-users"></i> Alunos Matriculados</h1>
        <span style="color: #666;">Total: <strong>${alunos.size()}</strong> alunos</span>
    </div>

    <div class="card-container">
        <ul class="student-list">
            <c:forEach var="aluno" items="${alunos}">
                <li class="student-item">

                    <div class="student-info">
                        <div class="avatar-circle">
                            <i class="fas fa-user-graduate"></i>
                        </div>
                        <div>
                            <div class="student-name">
                                <c:out value="${aluno.pnome}" /> <c:out value="${aluno.snome}" />
                            </div>
                        </div>
                    </div>

                    <div class="status-badge">
                        <i class="fas fa-check-circle"></i> Matriculado
                    </div>
                </li>
            </c:forEach>

            <c:if test="${empty alunos}">
                <div style="padding: 30px; text-align: center; color: #777;">
                    Nenhum aluno encontrado nesta disciplina.
                </div>
            </c:if>
        </ul>
    </div>

    <br>
    <a href="index.jsp">Voltar ao Dashboard</a>

</div>
</body>
</html>