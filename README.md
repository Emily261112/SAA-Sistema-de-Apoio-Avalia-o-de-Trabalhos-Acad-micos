# 🧾 SAA – Sistema de Apoio à Avaliação de Trabalhos Acadêmicos

## 📘 Descrição
O **SAA – Sistema de Apoio à Avaliação de Trabalhos Acadêmicos** tem como objetivo **auxiliar professores** na avaliação de trabalhos realizados por alunos em disciplinas e projetos acadêmicos.  

O sistema permite **cadastrar trabalhos, critérios e prazos**, realizar **avaliações individuais** e gerar **relatórios detalhados de desempenho**, baseados em critérios definidos.  

Com isso, o SAA torna o processo de correção **mais organizado, transparente e eficiente**, oferecendo aos docentes **estatísticas, médias e comparativos** de desempenho entre alunos e turmas.

---

## 🎯 Objetivos do Sistema
* Automatizar e padronizar o processo de **avaliação de trabalhos acadêmicos**.  
* Permitir o **cadastro de trabalhos, critérios e notas**.  
* Gerar **relatórios e gráficos estatísticos** para análise de desempenho.  
* Aplicar na prática os conceitos de **banco de dados, SQL e desenvolvimento web em camadas**.

---

## ⚙️ Funcionalidades Principais
* Cadastro de professores e disciplinas;  
* Cadastro de trabalhos e critérios de avaliação (peso, pontuação máxima);  
* Registro de avaliações individuais por aluno e trabalho;  
* Cálculo automático de médias e notas finais;  
* Relatórios com médias, evolução e comparativos de desempenho;  
* Controle de histórico de avaliações realizadas.

---

## 🧱 Modelagem de Dados
As principais entidades do sistema são:
* **Professor** – cadastra e realiza as avaliações;  
* **Disciplina** – agrupa os trabalhos;  
* **Trabalho** – contém as informações da avaliação;  
* **Critério** – define os parâmetros avaliativos (peso e nota máxima);  
* **Aluno** – representa o avaliado;  
* **Avaliação** – armazena notas, observações e resultados finais.

---

## 🧩 Diagrama Entidade-Relacionamento (ER)
O diagrama mostra as relações entre as entidades do sistema.  
  
   

---

## 📊 Relatórios Planejados
O sistema deverá gerar relatórios e gráficos que utilizam consultas SQL avançadas, incluindo junções, agrupamentos e funções de agregação.  
Exemplos de relatórios:
* Média de notas por trabalho;  
* Média por critério de avaliação;  
* Evolução temporal de desempenho dos alunos;  
* Ranking de notas;  
* Comparativo entre turmas ou períodos.  

---

## 🗃️ Banco de Dados
* **SGBD:** PostgreSQL  
* **Script:** disponível em `EntregaA/schema.sql`  
* O banco inclui restrições de integridade, chaves estrangeiras e índices.  

---

## 🖥️ Tecnologias
* **Back-end:** Java / J2EE (sem uso de JPA ou Hibernate)  
* **Front-end:** JavaScript (ou JSP/XHTML)  
