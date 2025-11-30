<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.TrabalhoDAO" %>
<%@ page import="org.example.dao.AvaliacaoDAO" %>
<%@ page import="org.example.model.RankingDTO" %>
<%@ page import="org.example.model.Avaliacao" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // 1. Pega o ID da avaliação
    String idStr = request.getParameter("id");
    if (idStr == null || idStr.isEmpty()) {
        response.sendRedirect("avaliacoes.jsp");
        return;
    }
    int idAvaliacao = Integer.parseInt(idStr);

    // 2. Busca o Ranking (Alunos e Notas)
    TrabalhoDAO trabalhoDAO = new TrabalhoDAO();
    List<RankingDTO> ranking = trabalhoDAO.getRankingAvaliacao(idAvaliacao);

    // 3. Busca os DETALHES da Avaliação (Para não mostrar só o ID)
    AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();
    Avaliacao avaliacao = avaliacaoDAO.buscarPorId(idAvaliacao);

    // 4. Guarda tudo no request
    request.setAttribute("ranking", ranking);
    request.setAttribute("avaliacao", avaliacao);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Lançar Notas - ${avaliacao.codAvaliacao}</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <style>
        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
        }

        .card-box {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.05);
            overflow: hidden;
            border: 1px solid #eee;
        }

        table { width: 100%; border-collapse: collapse; border-spacing: 0; }
        thead { background-color: #f8f9fa; border-bottom: 2px solid #e9ecef; }
        th { text-align: left; padding: 18px 25px; color: #495057; font-weight: 600; text-transform: uppercase; font-size: 0.85em; letter-spacing: 0.5px; }
        td { padding: 18px 25px; vertical-align: middle; border-bottom: 1px solid #f1f1f1; color: #555; }
        tr:last-child td { border-bottom: none; }
        tr:hover { background-color: #fcfcfc; }

        .rank-badge {
            display: inline-flex; align-items: center; justify-content: center;
            width: 32px; height: 32px; border-radius: 50%;
            font-weight: bold; font-size: 0.9em; background-color: #e9ecef; color: #555;
        }
        .rank-1 { background-color: #ffd700; color: #fff; text-shadow: 0 1px 2px rgba(0,0,0,0.2); }
        .rank-2 { background-color: #c0c0c0; color: #fff; text-shadow: 0 1px 2px rgba(0,0,0,0.2); }
        .rank-3 { background-color: #cd7f32; color: #fff; text-shadow: 0 1px 2px rgba(0,0,0,0.2); }

        .grade-value { font-weight: 700; font-size: 1.1em; color: #333; }
        .grade-total { font-size: 0.8em; color: #999; font-weight: normal; }

        .btn-correct {
            display: inline-block; background-color: #fff; color: #28a745;
            border: 1px solid #28a745; padding: 8px 16px; border-radius: 6px;
            text-decoration: none; font-weight: 600; font-size: 0.9em; transition: all 0.3s ease;
        }
        .btn-correct:hover { background-color: #28a745; color: #fff; transform: translateY(-1px); box-shadow: 0 2px 5px rgba(40, 167, 69, 0.3); }
        .btn-correct i { margin-right: 5px; }

        .empty-state { padding: 40px; text-align: center; color: #777; }
    </style>
</head>
<body>
<div class="container">

    <div class="page-header">
        <div>
            <h1 style="margin-bottom: 5px;"><i class="fas fa-pen-fancy"></i> Lançar Notas</h1>
            <p style="color: #666; margin: 0;">
                Gerencie as correções de:
                <strong style="color: #0056b3;">
                    ${avaliacao.codAvaliacao} - ${avaliacao.disciplina.nome}
                </strong>
            </p>
        </div>
        <a href="avaliacoes.jsp" class="btn-action" style="background-color: #6c757d;">
            <i class="fas fa-arrow-left"></i> Voltar
        </a>
    </div>

    <div class="card-box">
        <table>
            <thead>
            <tr>
                <th style="width: 80px; text-align: center;">Posição</th>
                <th>Aluno</th>
                <th>Nota Atual</th>
                <th style="text-align: right;">Ação</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${ranking}">
                <tr>
                    <td style="text-align: center;">
                        <c:choose>
                            <c:when test="${item.rank == 1}"><span class="rank-badge rank-1">1º</span></c:when>
                            <c:when test="${item.rank == 2}"><span class="rank-badge rank-2">2º</span></c:when>
                            <c:when test="${item.rank == 3}"><span class="rank-badge rank-3">3º</span></c:when>
                            <c:otherwise><span class="rank-badge">${item.rank}º</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <div style="font-weight: 600; color: #333;">${item.nomeEstudante}</div>
                    </td>
                    <td>
                        <span class="grade-value">${item.notaFinal}</span>
                        <span class="grade-total">/ 10.0</span>
                    </td>
                    <td style="text-align: right;">
                        <a href="corrigir_prova.jsp?idAvaliacao=${avaliacao.idAvaliacao}&idAluno=${item.idAluno}" class="btn-correct">
                            <i class="fas fa-check-double"></i> Corrigir Prova
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <c:if test="${empty ranking}">
            <div class="empty-state">
                <i class="fas fa-folder-open" style="font-size: 3em; color: #ddd; margin-bottom: 15px;"></i>
                <p>Nenhum aluno realizou esta avaliação ainda.</p>
            </div>
        </c:if>
    </div>

</div>
</body>
</html>