CREATE TABLE centro_custo (
    id         BIGSERIAL    PRIMARY KEY,
    nome       VARCHAR(255) NOT NULL,
    ies        VARCHAR(255) NOT NULL,
    status     VARCHAR(20)  NOT NULL DEFAULT 'ATIVO',
    tipo       VARCHAR(31)  NOT NULL  -- discriminator: 'ESCOLA' ou 'UNIDADE'
);

-- Tabela: escola (herda de centro_custo — joined table)
CREATE TABLE escola (
    id          BIGINT PRIMARY KEY REFERENCES centro_custo(id),
    coordenador VARCHAR(255) NOT NULL
);

-- Tabela: unidade
CREATE TABLE unidade (
    id          BIGINT PRIMARY KEY REFERENCES centro_custo(id),
    responsavel VARCHAR(255) NOT NULL
);

-- Tabela: usuario
CREATE TABLE usuario (
    id       BIGSERIAL    PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL
);

-- Tabela: profissional_saude
CREATE TABLE profissional_saude (
    id           BIGSERIAL    PRIMARY KEY,
    nome         VARCHAR(255) NOT NULL,
    especialidade VARCHAR(255),
    conselho     VARCHAR(255),
    numero_registro VARCHAR(255),
    status       VARCHAR(20)  NOT NULL DEFAULT 'ATIVO',
    usuario_id   BIGINT UNIQUE REFERENCES usuario(id)
);

-- Tabela: paciente
CREATE TABLE paciente (
    id         BIGSERIAL    PRIMARY KEY,
    nome       VARCHAR(255) NOT NULL,
    categoria  VARCHAR(50)  NOT NULL,
    email      VARCHAR(255),
    telefone   VARCHAR(20),
    status     VARCHAR(20)  NOT NULL DEFAULT 'ATIVO'
);

-- Tabela: prontuario (criado automaticamente com o paciente — RN007)
CREATE TABLE prontuario (
    id         BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL UNIQUE REFERENCES paciente(id)
);

-- Tabela: medicacao
CREATE TABLE medicacao (
    id        BIGSERIAL    PRIMARY KEY,
    nome      VARCHAR(255) NOT NULL,
    descricao TEXT,
    estoque   INT          NOT NULL DEFAULT 0,
    validade  DATE         NOT NULL,
    status    VARCHAR(20)  NOT NULL DEFAULT 'ATIVO'
);

-- Tabela: atendimento
CREATE TABLE atendimento (
    id                   BIGSERIAL    PRIMARY KEY,
    data_inicio          TIMESTAMP    NOT NULL,
    data_fim             TIMESTAMP,
    sintomas             TEXT,
    diagnostico          TEXT,
    tratamento           TEXT,
    tipo                 VARCHAR(20)  NOT NULL,
    prontuario_id        BIGINT       NOT NULL REFERENCES prontuario(id),
    profissional_saude_id BIGINT      NOT NULL REFERENCES profissional_saude(id)
);

-- Tabela: item_atendimento (medicação usada em um atendimento)
CREATE TABLE item_atendimento (
    id             BIGSERIAL PRIMARY KEY,
    quantidade     INT          NOT NULL,
    dosagem        VARCHAR(255),
    atendimento_id BIGINT       NOT NULL REFERENCES atendimento(id),
    medicacao_id   BIGINT       NOT NULL REFERENCES medicacao(id)
);

-- Tabela: requisicao_medicacao
CREATE TABLE requisicao_medicacao (
    id           BIGSERIAL PRIMARY KEY,
    tipo         VARCHAR(20)  NOT NULL,
    medicacao_id BIGINT       NOT NULL REFERENCES medicacao(id)
);