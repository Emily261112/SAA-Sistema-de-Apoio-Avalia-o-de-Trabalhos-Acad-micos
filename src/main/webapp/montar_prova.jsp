<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.AvaliacaoDAO" %>
<%@ page import="org.example.dao.QuestaoDAO" %>
<%@ page import="org.example.model.Avaliacao" %>
<%@ page import="org.example.model.Questao" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String idAvaliacaoStr = request.getParameter("idAvaliacao");
    if (idAvaliacaoStr == null || idAvaliacaoStr.isEmpty()) {
        response.sendRedirect("avaliacoes.jsp");
        return;
    }
    int idAvaliacao = Integer.parseInt(idAvaliacaoStr);

    AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();
    Avaliacao avaliacao = avaliacaoDAO.buscarPorId(idAvaliacao);
    request.setAttribute("avaliacao", avaliacao);

    QuestaoDAO questaoDAO = new QuestaoDAO();

    // Busca questões disponíveis e já adicionadas
    List<Questao> questoesDisponiveis = questaoDAO.buscarQuestoesDisponiveis(avaliacao.getDisciplina().getIdDisciplina(), idAvaliacao);
    request.setAttribute("questoesDisponiveis", questoesDisponiveis);

    List<Questao> questoesNaProva = questaoDAO.buscarQuestoesDaProva(idAvaliacao);
    request.setAttribute("questoesNaProva", questoesNaProva);
%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Montar Avaliação: ${avaliacao.codAvaliacao}</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .split-container { display: flex; gap: 30px; margin-top: 25px; }

        .split-column {
            flex: 1;
            background: #fff;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            border: 1px solid #eee;
        }

        .section-title {
            font-size: 1.4em;
            color: #333;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #f0f0f0;
            font-weight: bold; /* Deixei negrito pra compensar a falta do ícone */
        }

        .question-list { list-style: none; padding: 0; max-height: 400px; overflow-y: auto; }

        .question-item {
            padding: 12px 15px;
            border-bottom: 1px solid #f5f5f5;
            display: flex;
            align-items: flex-start;
            gap: 12px;
            transition: background 0.2s;
        }
        .question-item:hover { background-color: #f9fbfd; }
        .question-item:last-child { border-bottom: none; }

        .custom-checkbox { width: 18px; height: 18px; cursor: pointer; margin-top: 3px; }
        .question-text { flex: 1; color: #555; line-height: 1.4; }

        .question-type-badge {
            font-size: 0.75em;
            background-color: #e9ecef;
            color: #666;
            padding: 3px 8px;
            border-radius: 12px;
            margin-left: 8px;
            white-space: nowrap;
        }

        .modern-textarea {
            width: 100%;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            resize: vertical;
            font-family: inherit;
            margin-bottom: 20px;
            box-sizing: border-box;
        }
        .modern-textarea:focus { border-color: #007bff; outline: none; }

        .btn-primary-action, .btn-success-action {
            width: 100%;
            padding: 12px;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 1em;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.3s;
            text-align: center; /* Centralizado */
        }

        .btn-primary-action { background-color: #007bff; }
        .btn-primary-action:hover { background-color: #0056b3; }

        .btn-success-action { background-color: #28a745; }
        .btn-success-action:hover { background-color: #218838; }

        .added-questions-area {
            margin-top: 30px;
            background: #fff;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.03);
        }

        .header-info {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            border-left: 5px solid #007bff;
        }
    </style>
</head>
<body>
<div class="container">

    <div class="header-info">
        <h1 style="margin: 0; color: #333;">Montando Avaliação: <strong>${avaliacao.codAvaliacao}</strong></h1>
        <p style="margin: 10px 0 0; color: #666; font-size: 1.1em;">
            Disciplina: <strong>${avaliacao.disciplina.nome}</strong> |
            Máximo de Questões: <strong>${avaliacao.maxQuestoes}</strong>
        </p>
    </div>

    <div class="split-container">

        <div class="split-column">
            <h2 class="section-title">Banco de Questões</h2>
            <p style="color: #777; margin-bottom: 15px;">Selecione questões já cadastradas nesta disciplina:</p>

            <form action="vincularQuestao" method="post">
                <input type="hidden" name="idAvaliacao" value="${avaliacao.idAvaliacao}">

                <ul class="question-list">
                    <c:forEach var="q" items="${questoesDisponiveis}">
                        <li class="question-item">
                            <input type="checkbox" name="idsSelecionados" value="${q.idQuestao}" id="q_${q.idQuestao}" class="custom-checkbox">
                            <label for="q_${q.idQuestao}" class="question-text">
                                <c:out value="${q.enunciado}" />
                                <span class="question-type-badge">${q.tipo}</span>
                            </label>
                        </li>
                    </c:forEach>
                </ul>

                <c:if test="${empty questoesDisponiveis}">
                    <p style="color: #999; font-style: italic; padding: 20px; text-align: center;">Nenhuma questão disponível no banco.</p>
                </c:if>

                <div style="margin-top: 20px;">
                    <button type="submit" class="btn-primary-action">
                        Adicionar Selecionadas
                    </button>
                </div>
            </form>
        </div>

        <div class="split-column" style="background-color: #f9fbfd; border-color: #eef2f7;">
            <h2 class="section-title">Criar Nova Questão</h2>
            <p style="color: #777; margin-bottom: 20px;">Cadastra uma nova questão discursiva e já adiciona nesta prova.</p>

            <form action="salvarNovaQuestao" method="post">
                <input type="hidden" name="idAvaliacao" value="${avaliacao.idAvaliacao}">
                <input type="hidden" name="idDisciplina" value="${avaliacao.disciplina.idDisciplina}">
                <input type="hidden" name="tipo" value="Discursiva">

                <label for="enunciado" style="font-weight: bold; display: block; margin-bottom: 8px; color: #555;">Enunciado da Questão:</label>
                <textarea id="enunciado" name="enunciado" rows="6" class="modern-textarea" placeholder="Digite aqui o enunciado da sua nova questão..." required></textarea>

                <button type="submit" class="btn-success-action">
                    Salvar e Adicionar
                </button>
            </form>
        </div>
    </div>

    <div class="added-questions-area">
        <h2 class="section-title" style="border-bottom: none; margin-bottom: 15px;">Questões já presentes nesta prova:</h2>

        <c:if test="${not empty questoesNaProva}">
            <ul class="question-list" style="max-height: none; border-top: 1px solid #eee;">
                <c:forEach var="q" items="${questoesNaProva}" varStatus="status">
                    <li class="question-item" style="background: none; cursor: default;">
                        <strong style="color: #007bff; margin-right: 10px;">${status.count})</strong>
                        <span class="question-text">
                            <c:out value="${q.enunciado}" />
                        </span>
                    </li>
                </c:forEach>
            </ul>
        </c:if>

        <c:if test="${empty questoesNaProva}">
            <p style="color: #999; font-style: italic; padding: 10px;">Nenhuma questão adicionada ainda.</p>
        </c:if>
    </div>

    <br>
    <a href="index.jsp" style="display: inline-block; margin-top: 20px; color: #007bff; text-decoration: none;">
        Voltar para o Dashboard
    </a>

</div>
</body>
</html>