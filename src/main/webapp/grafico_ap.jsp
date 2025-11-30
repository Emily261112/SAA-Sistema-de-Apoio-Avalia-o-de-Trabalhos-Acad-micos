<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.TrabalhoDAO" %>
<%@ page import="org.example.dao.AvaliacaoDAO" %>
<%@ page import="org.example.model.RankingDTO" %>
<%@ page import="org.example.model.Avaliacao" %>
<%@ page import="org.example.model.Professor" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // 1. Segurança
    Object usuario = session.getAttribute("usuarioLogado");
    if (usuario == null || !(usuario instanceof Professor)) {
        response.sendRedirect("login.jsp");
        return;
    }

    String idParam = request.getParameter("id");
    if (idParam == null || idParam.isEmpty()) {
        response.sendRedirect("relatorios.jsp");
        return;
    }
    int idAvaliacao = Integer.parseInt(idParam);

    // 2. Busca o Ranking
    TrabalhoDAO trabalhoDAO = new TrabalhoDAO();
    List<RankingDTO> ranking = trabalhoDAO.getRankingAvaliacao(idAvaliacao);

    // 3. Busca detalhes da Avaliação (Para o título bonito)
    AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();
    Avaliacao avaliacao = avaliacaoDAO.buscarPorId(idAvaliacao);

    request.setAttribute("ranking", ranking);
    request.setAttribute("avaliacao", avaliacao);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Gráfico - ${avaliacao.codAvaliacao}</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .page-header {
            margin-bottom: 25px;
            border-bottom: 1px solid #eee;
            padding-bottom: 15px;
        }

        .chart-container {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.05);
            padding: 30px;
            border: 1px solid #e0e0e0;
        }

        .bar-group {
            margin-bottom: 20px;
            display: flex;
            align-items: center;
        }

        .bar-label {
            width: 180px;
            text-align: right;
            padding-right: 15px;
            font-weight: 600;
            color: #444;
            font-size: 0.95em;
        }

        .bar-track {
            flex: 1;
            background-color: #f1f3f5;
            border-radius: 4px;
            height: 35px;
            overflow: hidden; /* Garante bordas arredondadas */
        }

        .bar {
            height: 100%;
            color: white;
            text-align: right;
            padding-right: 10px;
            line-height: 35px;
            font-weight: bold;
            font-size: 0.9em;
            transition: width 0.6s ease-in-out;
            display: flex;
            align-items: center;
            justify-content: flex-end;
        }

        /* Legenda Limpa */
        .legend-box {
            display: flex;
            justify-content: center;
            gap: 30px;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #f0f0f0;
        }

        .legend-item {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 0.9em;
            color: #555;
            font-weight: 600;
        }

        .dot {
            width: 12px;
            height: 12px;
            border-radius: 3px;
        }

        .empty-state {
            text-align: center;
            padding: 40px;
            color: #888;
        }
    </style>
</head>
<body>
<div class="container">

    <div class="page-header">
        <h1 style="margin: 0; color: #333;">
            <i class="fas fa-chart-bar" style="color: #007bff;"></i>
            Desempenho: <strong>${avaliacao.codAvaliacao}</strong>
        </h1>
        <p style="margin: 5px 0 0; color: #666;">
            Disciplina: <strong>${avaliacao.disciplina.nome}</strong>
        </p>
    </div>

    <div class="chart-container">
        <c:forEach var="item" items="${ranking}">

            <c:set var="corBarra" value="#28a745" /> <c:if test="${item.notaFinal < 6.0}">
            <c:set var="corBarra" value="#ffc107" /> </c:if>
            <c:if test="${item.notaFinal < 3.0}">
                <c:set var="corBarra" value="#dc3545" /> </c:if>

            <div class="bar-group">
                <div class="bar-label">
                        ${item.nomeEstudante}
                </div>
                <div class="bar-track">
                    <div class="bar" style="width: ${item.notaFinal * 10}%; background-color: ${corBarra};">
                            ${item.notaFinal}
                    </div>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty ranking}">
            <div class="empty-state">
                <i class="fas fa-inbox" style="font-size: 3em; color: #e0e0e0; margin-bottom: 15px;"></i>
                <p>Nenhuma nota lançada para esta avaliação ainda.</p>
            </div>
        </c:if>

        <c:if test="${not empty ranking}">
            <div class="legend-box">
                <div class="legend-item">
                    <div class="dot" style="background-color: #28a745;"></div> Aprovado
                </div>
                <div class="legend-item">
                    <div class="dot" style="background-color: #ffc107;"></div> Exame
                </div>
                <div class="legend-item">
                    <div class="dot" style="background-color: #dc3545;"></div> Reprovado
                </div>
            </div>
        </c:if>
    </div>

    <br>
    <a href="relatorios.jsp" class="btn-action" style="background-color: #6c757d; display: inline-block;">
        <i class="fas fa-arrow-left"></i> Voltar aos Relatórios
    </a>
</div>
</body>
</html>