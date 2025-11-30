<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.model.Professor" %>
<%
    // Segurança
    Object usuario = session.getAttribute("usuarioLogado");
    if (usuario == null || !(usuario instanceof Professor)) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<html>
<head>
    <title>Nova Questão</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">
    <h1>Adicionar ao Banco de Questões</h1>
    <p style="color: #666; margin-bottom: 20px;">
        Cadastre perguntas aqui para reutilizar em futuras avaliações.
    </p>

    <% if ("sucesso".equals(request.getParameter("msg"))) { %>
    <div style="background-color: #d4edda; color: #155724; padding: 15px; border-radius: 5px; margin-bottom: 20px; border: 1px solid #c3e6cb;">
        ✅ <strong>Sucesso!</strong> Questão salva no banco. Cadastre a próxima.
    </div>
    <% } %>

    <form action="cadastrarQuestao" method="POST">

        <div>
            <label for="tipo">Tipo da Questão:</label>
            <select name="tipo" id="tipo">
                <option value="Discursiva">Discursiva (Texto Livre)</option>
                <option value="Multipla Escolha">Múltipla Escolha</option>
            </select>
        </div>

        <div>
            <label for="enunciado">Enunciado da Pergunta:</label>
            <textarea name="enunciado" id="enunciado" rows="4" required placeholder="Ex: O que é uma Chave Primária?"></textarea>
        </div>

        <button type="submit" class="salvar">Salvar no Banco</button>
    </form>

    <br>
    <a href="index.jsp">Voltar ao Dashboard</a>
</div>
</body>
</html>