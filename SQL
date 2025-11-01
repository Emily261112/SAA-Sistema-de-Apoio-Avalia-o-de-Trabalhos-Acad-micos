
-- Criação das tabelas com base no modelo relacional aprovado

CREATE TABLE Usuario (
    id_usuario INT PRIMARY KEY,
    pnome VARCHAR(50) NOT NULL,
    snome VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE Estudante (
    id_usuario INT PRIMARY KEY,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
);

CREATE TABLE Professor (
    id_usuario INT PRIMARY KEY,
    cria_aval_cod INT,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
);

CREATE TABLE Disciplina (
    id_disciplina INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    adm_prof_id INT NOT NULL,
    FOREIGN KEY (adm_prof_id) REFERENCES Professor(id_usuario)
);

CREATE TABLE Avaliacao (
    id_disciplina INT NOT NULL,
    cod_avaliacao INT NOT NULL,
    cria_aval_cod INT,
    data_publicacao DATE,
    prazo DATE,
    PRIMARY KEY (id_disciplina, cod_avaliacao),
    FOREIGN KEY (id_disciplina) REFERENCES Disciplina(id_disciplina),
    FOREIGN KEY (cria_aval_cod) REFERENCES Professor(id_usuario)
);

CREATE TABLE Criterio (
    id_disciplina INT NOT NULL,
    cod_avaliacao INT NOT NULL,
    define_id_prof INT NOT NULL,
    id_criterio INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    peso DECIMAL(4,2) CHECK (peso >= 0 AND peso <= 10),
    PRIMARY KEY (id_disciplina, cod_avaliacao, define_id_prof, id_criterio),
    FOREIGN KEY (id_disciplina, cod_avaliacao) REFERENCES Avaliacao(id_disciplina, cod_avaliacao),
    FOREIGN KEY (define_id_prof) REFERENCES Professor(id_usuario)
);

CREATE TABLE Trabalho (
    pert_aval_cod INT NOT NULL,
    id_usuario INT NOT NULL,
    id_disciplina INT NOT NULL,
    data_envio DATE NOT NULL,
    arquivo_url TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'Não Avaliado',
    PRIMARY KEY (pert_aval_cod, id_usuario),
    FOREIGN KEY (id_usuario) REFERENCES Estudante(id_usuario),
    FOREIGN KEY (id_disciplina, pert_aval_cod) REFERENCES Avaliacao(id_disciplina, cod_avaliacao)
);

CREATE TABLE Est_part_discp (
    id_usuario INT NOT NULL,
    id_disciplina INT NOT NULL,
    PRIMARY KEY (id_usuario, id_disciplina),
    FOREIGN KEY (id_usuario) REFERENCES Estudante(id_usuario),
    FOREIGN KEY (id_disciplina) REFERENCES Disciplina(id_disciplina)
);
