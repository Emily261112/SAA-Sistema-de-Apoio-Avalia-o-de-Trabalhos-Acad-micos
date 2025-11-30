<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.AvaliacaoDAO" %>
<%@ page import="org.example.model.Professor" %>
<%@ page import="org.example.model.Avaliacao" %>
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

    // Busca avaliações para o dropdown
    AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();
    List<Avaliacao> avaliacoes = avaliacaoDAO.findAvaliacoesByProfessor(professor.getIdUsuario());
    request.setAttribute("avaliacoes", avaliacoes);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Central de Relatórios</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .page-header {
            margin-bottom: 30px;
            border-bottom: 2px solid #f0f0f0;
            padding-bottom: 15px;
        }

        .report-grid {
            display: grid;
            grid-template-columns: 1fr; /* Uma coluna (empilhado) */
            gap: 25px;
        }

        .report-card {
            background: #fff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.05);
            border: 1px solid #eee;
            transition: transform 0.2s, box-shadow 0.2s;
            display: flex;
            flex-direction: column;
        }

        .report-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
        }

        .card-header {
            display: flex;
            align-items: center;
            gap: 15px;
            margin-bottom: 15px;
        }

        .icon-box {
            width: 50px;
            height: 50px;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
        }

        .icon-blue { background-color: #e3f2fd; color: #1976d2; }
        .icon-purple { background-color: #f3e5f5; color: #7b1fa2; }

        .card-title {
            font-size: 1.25em;
            color: #333;
            margin: 0;
            font-weight: 700;
        }

        .card-desc {
            color: #666;
            margin-bottom: 25px;
            line-height: 1.5;
        }

        /* Estilização do Formulário e Select */
        .form-inline {
            display: flex;
            gap: 10px;
            align-items: center;
        }

        .styled-select {
            flex: 1;
            padding: 12px 15px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 1em;
            color: #333;
            background-color: #fff;
            cursor: pointer;
            transition: border-color 0.3s;
        }

        .styled-select:focus {
            border-color: #007bff;
            outline: none;
        }

        /* Botões */
        .btn-report {
            padding: 12px 20px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: 600;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            transition: background 0.3s;
            cursor: pointer;
            border: none;
            font-size: 1em;
        }

        .btn-primary-report {
            background-color: #007bff;
            color: white;
            width: fit-content;
        }
        .btn-primary-report:hover { background-color: #0056b3; }

        .btn-dark-report {
            background-color: #343a40;
            color: white;
        }
        .btn-dark-report:hover { background-color: #23272b; }

    </style>
</head>
<body>
<div class="container">

    <div class="page-header">
        <h1 style="margin: 0; color: #333;"><i class="fas fa-chart-pie"></i> Central de Relatórios</h1>
        <p style="color: #666; margin-top: 5px;">Analise o desempenho da turma e estatísticas das avaliações.</p>
    </div>

    <div class="report-grid">

        <div class="report-card">
            <div class="card-header">
                <div class="icon-box icon-blue">
                    <i class="fas fa-layer-group"></i>
                </div>
                <h3 class="card-title">Desempenho Geral da Turma</h3>
            </div>
            <p class="card-desc">
                Visualize um gráfico de barras comparativo com a <strong>Média Final Acumulada</strong> de todos os alunos, considerando todas as provas realizadas até o momento.
            </p>
            <a href="grafico_medias.jsp" class="btn-report btn-primary-report">
                <i class="fas fa-chart-bar"></i> Ver Gráfico de Médias
            </a>
        </div>

        <div class="report-card">
            <div class="card-header">
                <div class="icon-box icon-purple">
                    <i class="fas fa-trophy"></i>
                </div>
                <h3 class="card-title">Ranking por Avaliação Específica</h3>
            </div>
            <p class="card-desc">
                Selecione uma avaliação abaixo para ver o ranking detalhado, quem foi aprovado, quem ficou de recuperação e as notas individuais daquela prova.
            </p>

            <form action="grafico_ap.jsp" method="GET" class="form-inline">
                <select name="id" required class="styled-select">
                    <option value="" disabled selected>Selecione uma Avaliação...</option>
                    <c:forEach var="av" items="${avaliacoes}">
                        <option value="${av.idAvaliacao}">
                                ${av.codAvaliacao} - ${av.disciplina.nome} (Prazo: ${av.prazo})
                        </option>
                    </c:forEach>
                </select>

                <button type="submit" class="btn-report btn-dark-report">
                    <i class="fas fa-search"></i> Gerar Gráfico
                </button>
            </form>
        </div>

    </div>

    <br>
    <a href="index.jsp" style="display: inline-block; margin-top: 20px; color: #007bff; text-decoration: none;">
        <i class="fas fa-arrow-left"></i> Voltar ao Dashboard
    </a>
</div>
</body>
</html>