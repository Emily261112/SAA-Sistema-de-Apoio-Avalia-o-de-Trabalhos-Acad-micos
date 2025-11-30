<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cadastro de Professor e Disciplina</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        /* Estilos específicos para esta página para garantir o visual */
        .container-form {
            background-color: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            max-width: 600px; /* Limita a largura para não ficar muito esticado */
            margin: 0 auto; /* Centraliza na tela */
        }

        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }

        h2 {
            color: #555;
            font-size: 1.2em;
            margin-bottom: 15px;
            margin-top: 25px;
            border-bottom: 2px solid #eee;
            padding-bottom: 10px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #444;
        }

        input[type="text"],
        input[type="email"],
        input[type="password"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box; /* Garante que o padding não aumente a largura */
            font-size: 14px;
            transition: border-color 0.3s;
        }

        input:focus {
            border-color: #007bff;
            outline: none;
        }

        .btn-submit {
            width: 100%;
            padding: 12px;
            background-color: #28a745; /* Verde sucesso */
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s;
            margin-top: 20px;
        }

        .btn-submit:hover {
            background-color: #218838;
        }

        .back-link {
            display: block;
            text-align: center;
            margin-top: 20px;
            color: #007bff;
            text-decoration: none;
        }
        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="container container-form">

    <h1>Cadastro Inicial</h1>
    <p style="text-align: center; color: #666; margin-bottom: 30px;">Preencha os dados para criar o primeiro professor e sua disciplina.</p>

    <form action="cadastrarProfessor" method="post">

        <h2><i class="fas fa-user-tie"></i> Dados Pessoais do Professor</h2>

        <div class="form-group">
            <label for="pnome">Primeiro Nome:</label>
            <input type="text" id="pnome" name="pnome" placeholder="Ex: João" required>
        </div>

        <div class="form-group">
            <label for="snome">Sobrenome:</label>
            <input type="text" id="snome" name="snome" placeholder="Ex: Silva" required>
        </div>

        <div class="form-group">
            <label for="email">Email (Login):</label>
            <input type="email" id="email" name="email" placeholder="joao.silva@escola.com" required>
        </div>

        <div class="form-group">
            <label for="senha">Senha:</label>
            <input type="password" id="senha" name="senha" placeholder="********" required>
        </div>

        <h2><i class="fas fa-book"></i> Dados da Disciplina Inicial</h2>
        <p style="font-size: 0.9em; color: #777; margin-bottom: 15px;">Esta disciplina será vinculada automaticamente a este professor.</p>

        <div class="form-group">
            <label for="nomeDisciplina">Nome da Disciplina:</label>
            <input type="text" id="nomeDisciplina" name="nomeDisciplina" placeholder="Ex: Algoritmos e Programação" required>
        </div>

        <button type="submit" class="btn-submit">Cadastrar Professor e Disciplina</button>

    </form>

    <a href="index.jsp" class="back-link">Voltar para a Tela Inicial</a>

</div>

<script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>

</body>
</html>