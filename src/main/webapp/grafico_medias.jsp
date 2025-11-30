<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.TrabalhoDAO" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="org.example.model.Professor" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // 1. Segurança
    Object usuario = session.getAttribute("usuarioLogado");
    if (usuario == null || !(usuario instanceof Professor)) {
        response.sendRedirect("login.jsp");
        return;
    }

    // 2. Busca os dados
    TrabalhoDAO trabalhoDAO = new TrabalhoDAO();
    Map<String, BigDecimal> medias = trabalhoDAO.getOverallFinalAverage();

    request.setAttribute("mediasGerais", medias);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Gráfico de Médias Finais</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .page-header {
            margin-bottom: 25px;
            border-bottom: 1px solid #eee;
            padding-bottom: 15px;
        }

        .chart-container {
            margin-top: 30px;
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

        /* O "Trilho" cinza atrás da barra */
        .bar-track {
            flex: 1;
            background-color: #f1f3f5;
            border-radius: 4px;
            height: 35px;
            overflow: hidden;
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
            min-width: 40px;
        }

        /* Legenda Limpa e Centralizada */
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
            <i class="fas fa-chart-line" style="color: #007bff;"></i>
            Média Final da Turma
        </h1>
        <p style="color: #666; margin-top: 5px;">Desempenho acumulado considerando todas as avaliações.</p>
    </div>

    <div class="chart-container">

        <c:forEach var="entry" items="${mediasGerais}">

            <c:set var="corBarra" value="#28a745" /> <c:if test="${entry.value < 6.0}">
            <c:set var="corBarra" value="#ffc107" /> </c:if>
            <c:if test="${entry.value < 3.0}">
                <c:set var="corBarra" value="#dc3545" /> </c:if>

            <div class="bar-group">
                <div class="bar-label">${entry.key}</div>

                <div class="bar-track">
                    <div class="bar" style="width: ${entry.value * 10}%; max-width: 100%; background-color: ${corBarra};">
                            ${entry.value}
                    </div>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty mediasGerais}">
            <div class="empty-state">
                <i class="fas fa-inbox" style="font-size: 3em; color: #e0e0e0; margin-bottom: 15px;"></i>
                <p>Nenhuma nota lançada ainda.</p>
            </div>
        </c:if>

        <c:if test="${not empty mediasGerais}">
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