<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.dao.RespostaDAO" %>
<%@ page import="org.example.dao.AvaliacaoDAO" %>
<%@ page import="org.example.dao.UsuarioDAO" %>
<%@ page import="org.example.model.Resposta" %>
<%@ page import="org.example.model.Avaliacao" %>
<%@ page import="org.example.model.Usuario" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // 1. Recebe os IDs da URL
    int idAvaliacao = Integer.parseInt(request.getParameter("idAvaliacao"));
    int idAluno = Integer.parseInt(request.getParameter("idAluno"));

    // 2. Busca as Respostas (Isso já existia)
    RespostaDAO respostaDAO = new RespostaDAO();
    List<Resposta> respostas = respostaDAO.buscarRespostasDoAluno(idAvaliacao, idAluno);
    request.setAttribute("respostas", respostas);

    // 3. NOVO: Busca os detalhes da Avaliação (Para mostrar "AP1 - Algoritmos")
    AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();
    Avaliacao avaliacao = avaliacaoDAO.buscarPorId(idAvaliacao);

    // 4. NOVO: Busca os detalhes do Aluno (Para mostrar "Lucas Mendes")
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    Usuario aluno = usuarioDAO.buscarPorId(idAluno);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Corrigir Prova - ${aluno.pnome}</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .header-info {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            border-left: 5px solid #007bff; /* Detalhe azul */
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
            margin-bottom: 25px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .header-info h2 { margin: 0; color: #333; font-size: 1.5em; }
        .header-info p { margin: 5px 0 0; color: #666; font-size: 1em; }

        .questao-box { background: #fff; padding: 25px; margin-bottom: 20px; border: 1px solid #e0e0e0; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.02); }
        .enunciado { font-weight: 700; color: #333; margin-bottom: 15px; display: block; font-size: 1.1em; }

        .resposta-aluno {
            background: #f8f9fa;
            padding: 15px;
            border-left: 4px solid #6c757d;
            color: #555;
            border-radius: 4px;
            margin-bottom: 15px;
        }

        .nota-area {
            display: flex;
            align-items: center;
            gap: 10px;
            background: #eefdf3; /* Fundo verde claro */
            padding: 10px 15px;
            border-radius: 6px;
            border: 1px solid #c3e6cb;
            width: fit-content;
        }
        input[type=number] { width: 70px; padding: 5px; font-weight: bold; border: 1px solid #ccc; border-radius: 4px; }

        .btn-save {
            background-color: #28a745;
            color: white;
            padding: 12px 25px;
            font-size: 16px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 8px;
            margin-top: 10px;
        }
        .btn-save:hover { background-color: #218838; }
    </style>
</head>
<body>
<div class="container">

    <div class="header-info">
        <div>
            <h2><i class="fas fa-user-graduate"></i> <%= aluno.getPnome() %> <%= aluno.getSnome() != null ? aluno.getSnome() : "" %></h2>
            <p>
                <i class="fas fa-file-alt"></i> Corrigindo: <strong><%= avaliacao.getCodAvaliacao() %></strong>
                - <%= avaliacao.getDisciplina().getNome() %>
            </p>
        </div>
        <div style="text-align: right;">
            <a href="lancar_nota.jsp?id=<%= idAvaliacao %>" class="btn-action" style="background:#6c757d; font-size: 0.9em;">
                Voltar
            </a>
        </div>
    </div>

    <form action="salvarCorrecao" method="post">
        <input type="hidden" name="idAvaliacao" value="<%= idAvaliacao %>">

        <c:forEach var="r" items="${respostas}" varStatus="status">
            <div class="questao-box">
                <span class="enunciado">
                    <span style="color: #007bff;">Questão ${status.count})</span> ${r.questao.enunciado}
                </span>

                <div class="resposta-aluno">
                    <div style="font-size: 0.8em; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 5px; color: #888;">
                        Resposta do Aluno:
                    </div>
                        ${r.respostaTexto}
                </div>

                <div class="nota-area">
                    <label><strong>Nota (0-10):</strong></label>
                    <input type="number" name="nota_${r.idResposta}"
                           step="0.1" min="0" max="10"
                           value="${r.notaObtida}" required>
                </div>
            </div>
        </c:forEach>

        <button type="submit" class="btn-save">
            <i class="fas fa-save"></i> Salvar Correção
        </button>
    </form>

    <br><br>

</div>
</body>
</html>