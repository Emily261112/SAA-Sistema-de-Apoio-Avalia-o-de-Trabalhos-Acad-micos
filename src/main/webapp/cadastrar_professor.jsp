<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cadastro Professor</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        form { width: 50%; border: 1px solid #ddd; padding: 20px; border-radius: 8px; }
        div { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="email"], input[type="password"] {
            width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;
        }
        button { background-color: #28a745; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; }
        a { color: #007bff; text-decoration: none; }
    </style>
</head>
<body>

<h1>Cadastro Inicial de Professor e Disciplina</h1>

<form action="cadastrarProfessor" method="POST">
    <h2>Dados Pessoais:</h2>
    <div>
        <label for="pnome">Primeiro Nome:</label>
        <input type="text" id="pnome" name="pnome" required>
    </div>

    <div>
        <label for="snome">Sobrenome:</label>
        <input type="text" id="snome" name="snome" required>
    </div>

    <div>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>
    </div>

    <div>
        <label for="senha">Senha:</label>
        <input type="password" id="senha" name="senha" required>
    </div>

    <hr>

    <h2>Dados da Disciplina (A ser administrada):</h2>
    <div>
        <label for="nomeDisciplina">Nome da Disciplina:</label>
        <input type="text" id="nomeDisciplina" name="nomeDisciplina" required>
    </div>

    <button type="submit">Cadastrar Professor e Disciplina</button>
</form>

<br/>
<a href="index.jsp">Voltar para o Dashboard</a>

</body>
</html>