<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login - Sistema Acadêmico</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="login-page"> <div class="login-card">

    <h1>Bem-vindo</h1>
    <p class="subtitle">Faça login para gerenciar suas avaliações</p>

    <c:if test="${not empty requestScope.erroLogin}">
        <div class="error-message">
            🚫 <c:out value="${requestScope.erroLogin}" />
        </div>
    </c:if>

    <form action="login" method="POST">
        <input type="email" name="email" placeholder="Seu e-mail" required autocomplete="off">
        <input type="password" name="senha" placeholder="Sua senha" required>

        <button type="submit" class="btn-login">ENTRAR</button>
    </form>

    <div class="login-footer">
        <p>Ainda não tem acesso?</p>
        <a href="cadastrar_professor.jsp">Cadastre sua Disciplina aqui</a>
    </div>

</div>

</body>
</html>