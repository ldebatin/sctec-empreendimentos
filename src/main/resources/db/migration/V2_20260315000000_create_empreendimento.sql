-- V2: Criação da tabela de empreendimentos
CREATE TABLE empreendimento (
    id        INTEGER     PRIMARY KEY AUTOINCREMENT,
    nome      VARCHAR(255) NOT NULL,
    nome_empreendedor VARCHAR(255) NOT NULL,
    municipio VARCHAR(255) NOT NULL,
    segmento  VARCHAR(50)  NOT NULL,
    email     VARCHAR(255) NOT NULL,
    telefone  VARCHAR(20)  NOT NULL,
    status    VARCHAR(10)  NOT NULL DEFAULT 'ATIVO'
);
