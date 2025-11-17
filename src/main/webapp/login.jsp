<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login - Avaliador</title>
    <style>
        body { font-family: Arial, sans-serif; display: flex; justify-content: center; align-items: center; min-height: 100vh; background-color: #f4f4f4; }
        .login-box { background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); width: 300px; text-align: center; }
        h1 { color: #333; margin-bottom: 20px; }
        input[type="email"], input[type="password"] { width: 100%; padding: 10px; margin-bottom: 15px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        button { width: 100%; background-color: #007bff; color: white; padding: 10px; border: none; border-radius: 4px; cursor: pointer; }
        .error { color: red; margin-bottom: 15px; }
        a { display: block; margin-top: 15px; color: #555; text-decoration: none; font-size: 0.9em; }
    </style>
</head>
<body>

<div class="login-box">
    <h1>Login Professor</h1>

    <c:if test="${not empty requestScope.erroLogin}">
        <div class="error"><c:out value="${requestScope.erroLogin}" /></div>
    </c:if>

    <form action="login" method="POST">
        <input type="email" name="email" placeholder="Email" required>
        <input type="password" name="senha" placeholder="Senha" required>
        <button type="submit">Entrar</button>
    </form>

    <hr>
    <a href="cadastrar_professor.jsp">Primeiro Acesso? Cadastre a Disciplina</a>
</div>

</body>
</html>