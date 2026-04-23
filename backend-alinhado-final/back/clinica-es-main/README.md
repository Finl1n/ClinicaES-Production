# Clínica Médica — Backend API

> API RESTful para gestão de uma clínica médica universitária, desenvolvida com **Java 21** e **Spring Boot 4.0.5**. Gerencia profissionais de saúde, pacientes, atendimentos, prontuários, medicamentos e requisições de medicação com controle de acesso baseado em perfis.

---

## Sumário

- [Visão Geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Módulos e Domínios](#módulos-e-domínios)
- [Endpoints da API](#endpoints-da-api)
- [Segurança e Autenticação](#segurança-e-autenticação)
- [Banco de Dados e Migrações](#banco-de-dados-e-migrações)
- [Como Executar](#como-executar)
- [Variáveis de Ambiente](#variáveis-de-ambiente)
- [Usuários Padrão](#usuários-padrão)

---

## Visão Geral

O sistema atende ao fluxo completo de uma clínica médica universitária, desde o cadastro de unidades e escolas vinculadas até o registro de atendimentos, geração automática de prontuários e controle de estoque de medicamentos. A API foi projetada para ser consumida por um frontend Angular e expõe um endpoint de dashboard com métricas consolidadas.

---

## Tecnologias

| Categoria          | Tecnologia                                          |
|--------------------|-----------------------------------------------------|
| Linguagem          | Java 21                                             |
| Framework          | Spring Boot 4.0.5                                   |
| Segurança          | Spring Security + JWT (Auth0 `java-jwt` 4.4.0)      |
| Persistência       | Spring Data JPA + Hibernate                         |
| Banco de Dados     | PostgreSQL 16                                       |
| Migrações          | Flyway (gerenciado via SQL versionado)              |
| Build              | Maven 3.9.6                                         |
| Containerização    | Docker + Docker Compose                             |
| Utilitários        | Lombok, Bean Validation (Jakarta)                   |

---

## Arquitetura

O projeto segue uma arquitetura em camadas com organização **por domínio**, mantendo a separação de responsabilidades dentro de cada módulo:

```
src/main/java/ucsal/clinica/medica/
│
├── config/              # SecurityConfig, CorsConfig, SystemStart (seed de usuários)
├── security/            # TokenService (JWT), UsuarioAutenticado (contexto da requisição)
├── exception/           # GlobalExceptionHandler
├── global/              # DashboardService, enums compartilhados (Status, CentroCusto)
│
├── auth/                # Autenticação: login, cadastro de usuário, roles
├── profissional/        # ProfissionalSaude: cadastro, complemento de perfil
├── paciente/            # Paciente: cadastro, vínculo com profissional
├── prontuario/          # Prontuário: gerado automaticamente por paciente
├── atendimento/         # Registro de atendimentos clínicos
├── medicamento/         # Catálogo de medicamentos e controle de status
├── requisicao/          # RequisicaoMedicacao + UsoMedicacao (saída de estoque)
├── unidade/             # Unidades da clínica (herança de CentroCusto)
└── escola/              # Escolas vinculadas (herança de CentroCusto)
```

Cada domínio contém seus próprios subpacotes `model`, `dto`, `mapper`, `repository`, `service` e é exposto por um `@RestController` centralizado em `controller/`.

---

## Módulos e Domínios

### Autenticação (`auth`)
Gerencia o ciclo de vida de usuários do sistema. Dois perfis existem: `ADMINISTRADOR` e `PROFISSIONAL_SAUDE`. O seed inicial cria os usuários padrão na inicialização via `SystemStart`.

### Profissional de Saúde (`profissional`)
Cadastro realizado pelo administrador, com complemento de perfil (especialidade, conselho, número de registro) feito pelo próprio profissional após o primeiro acesso. O profissional é vinculado a um `Usuario` do sistema.

### Paciente (`paciente`)
Cadastrado pelo profissional de saúde que o atende. Possui categoria (`CategoriaPaciente`) e vínculo (`VinculoPaciente`). Na criação, um **prontuário é gerado automaticamente** (regra de negócio RN007).

### Prontuário (`prontuario`)
Registro clínico vinculado 1:1 ao paciente. Agrega os atendimentos realizados. Leitura disponível por profissional, por ID ou por paciente.

### Atendimento (`atendimento`)
Registro de consultas e procedimentos. Criado pelo profissional autenticado. O administrador tem visão global; o profissional vê apenas seus próprios atendimentos.

### Medicamento (`medicamento`)
Catálogo de medicamentos com status ativo/inativo. Inclui tipo de armazenamento (`ArmazenamentoMedicacao`). Permite criação, atualização e toggle de ativação.

### Requisição de Medicação (`requisicao`)
Registra a saída de medicamentos do estoque vinculada a um atendimento. Gera registros de `UsoMedicacao` com rastreabilidade de quem prescreveu e quando.

### Unidade e Escola (`unidade`, `escola`)
Entidades de centro de custo da instituição, modeladas com **herança joined-table** (`CentroCusto` → `Escola` / `Unidade`). Utilizadas no vínculo de pacientes e profissionais à estrutura organizacional.

### Dashboard (`global`)
Endpoint consolidado que retorna métricas de status do sistema (total de pacientes, profissionais, atendimentos, medicamentos e requisições ativas).

---

## Endpoints da API

Todos os endpoints, exceto `/auth/login` e `/auth/sign-in`, requerem autenticação via token JWT no header `Authorization: Bearer <token>`.

### Autenticação

| Método | Rota           | Permissão | Descrição                        |
|--------|----------------|-----------|----------------------------------|
| POST   | `/auth/login`  | Pública   | Realiza login e retorna o JWT    |
| POST   | `/auth/sign-in`| Pública   | Alias de `/auth/login`           |

### Profissionais de Saúde

| Método | Rota                               | Permissão          | Descrição                              |
|--------|------------------------------------|--------------------|----------------------------------------|
| POST   | `/profissionais`                   | ADMINISTRADOR      | Cadastra novo profissional             |
| GET    | `/profissionais`                   | ADMINISTRADOR      | Lista todos os profissionais ativos    |
| GET    | `/profissionais/{id}`              | ADMINISTRADOR      | Busca profissional por ID              |
| GET    | `/profissionais/meu-perfil`        | PROFISSIONAL_SAUDE | Consulta o próprio perfil              |
| PATCH  | `/profissionais/meu-perfil/complemento` | PROFISSIONAL_SAUDE | Complementa dados do perfil      |
| POST   | `/profissionais/complemento`       | PROFISSIONAL_SAUDE | Alias legado para complemento          |
| PATCH  | `/profissionais/{id}/inativar`     | ADMINISTRADOR      | Inativa um profissional                |
| POST   | `/profissionais/inativar/{id}`     | ADMINISTRADOR      | Alias legado para inativação           |

### Pacientes

| Método | Rota                   | Permissão                              | Descrição                                |
|--------|------------------------|----------------------------------------|------------------------------------------|
| GET    | `/paciente`            | ADMINISTRADOR / PROFISSIONAL_SAUDE     | Lista pacientes (admin: todos; profissional: os seus) |
| POST   | `/paciente`            | PROFISSIONAL_SAUDE                     | Cadastra paciente (gera prontuário automático) |
| PUT    | `/paciente/{id}`       | PROFISSIONAL_SAUDE                     | Atualiza dados do paciente               |
| PUT    | `/paciente/inativar/{id}` | PROFISSIONAL_SAUDE                  | Inativa um paciente                      |

### Prontuários

| Método | Rota                        | Permissão          | Descrição                              |
|--------|-----------------------------|--------------------|----------------------------------------|
| GET    | `/prontuario`               | PROFISSIONAL_SAUDE | Lista prontuários do profissional logado |
| GET    | `/prontuario/{id}`          | Autenticado        | Busca prontuário por ID                |
| GET    | `/prontuario/paciente/{id}` | Autenticado        | Lista prontuários de um paciente       |

### Atendimentos

| Método | Rota           | Permissão          | Descrição                                              |
|--------|----------------|--------------------|--------------------------------------------------------|
| GET    | `/atendimento` | Autenticado        | Lista atendimentos (admin: todos; profissional: os seus) |
| POST   | `/atendimento` | PROFISSIONAL_SAUDE | Registra novo atendimento                              |
| PUT    | `/atendimento` | Autenticado        | Atualiza atendimento existente                         |

### Medicamentos

| Método | Rota                         | Permissão   | Descrição                      |
|--------|------------------------------|-------------|--------------------------------|
| GET    | `/medicamento`               | Autenticado | Lista todos os medicamentos    |
| POST   | `/medicamento`               | Autenticado | Cadastra novo medicamento      |
| PUT    | `/medicamento`               | Autenticado | Atualiza medicamento           |
| PUT    | `/medicamento/inativar/{id}` | Autenticado | Ativa/inativa medicamento      |

### Requisições de Medicação

| Método | Rota          | Permissão          | Descrição                              |
|--------|---------------|--------------------|----------------------------------------|
| GET    | `/requisicoes`| Autenticado        | Lista todas as requisições             |
| POST   | `/requisicoes`| PROFISSIONAL_SAUDE | Cria requisição de medicação           |

### Unidades

| Método | Rota                      | Permissão                          | Descrição                    |
|--------|---------------------------|------------------------------------|------------------------------|
| POST   | `/unidades`               | ADMINISTRADOR                      | Cadastra unidade             |
| GET    | `/unidades`               | ADMINISTRADOR / PROFISSIONAL_SAUDE | Lista unidades ativas        |
| GET    | `/unidades/{id}`          | ADMINISTRADOR / PROFISSIONAL_SAUDE | Busca unidade por ID         |
| PUT    | `/unidades/{id}`          | ADMINISTRADOR                      | Atualiza unidade             |
| PATCH  | `/unidades/{id}/inativar` | ADMINISTRADOR                      | Inativa unidade              |
| POST   | `/unidades/inativar/{id}` | ADMINISTRADOR                      | Alias legado para inativação |

### Escolas

| Método | Rota                      | Permissão                          | Descrição                    |
|--------|---------------------------|------------------------------------|------------------------------|
| POST   | `/escolas`                | ADMINISTRADOR                      | Cadastra escola              |
| GET    | `/escolas`                | ADMINISTRADOR / PROFISSIONAL_SAUDE | Lista escolas ativas         |
| GET    | `/escolas/{id}`           | ADMINISTRADOR / PROFISSIONAL_SAUDE | Busca escola por ID          |
| PUT    | `/escolas/{id}`           | ADMINISTRADOR                      | Atualiza escola              |
| PATCH  | `/escolas/{id}/inativar`  | ADMINISTRADOR                      | Inativa escola               |

### Dashboard

| Método | Rota      | Permissão   | Descrição                                  |
|--------|-----------|-------------|--------------------------------------------|
| GET    | `/status` | Autenticado | Retorna métricas consolidadas do sistema   |

---

## Segurança e Autenticação

O sistema utiliza **autenticação stateless** com JWT. O fluxo é:

1. O cliente envia credenciais para `POST /auth/login`.
2. A API valida as credenciais e retorna um token JWT com validade de **2 horas** (7.200.000 ms).
3. O token deve ser enviado em todas as requisições subsequentes no header: `Authorization: Bearer <token>`.
4. O `SecurityFilter` intercepta cada requisição, valida o token via `TokenService` e popula o `SecurityContext`.
5. O controle de acesso por perfil é aplicado via `@PreAuthorize` nos controllers.

Os dois perfis disponíveis são:

- **`ADMINISTRADOR`** — gerencia profissionais, unidades, escolas e tem visão global de dados.
- **`PROFISSIONAL_SAUDE`** — cadastra e gerencia pacientes, realiza atendimentos e registra requisições de medicação.

---

## Banco de Dados e Migrações

O schema é gerenciado pelo **Flyway** com migrações SQL versionadas em `src/main/resources/db/migration/`:

| Versão | Descrição                                           |
|--------|-----------------------------------------------------|
| V1     | Criação das tabelas base (usuário, paciente, prontuário, medicação, atendimento, etc.) |
| V2     | Inserção do usuário administrador padrão            |
| V3     | Adição de campos de cadastro completo para profissional |
| V4     | Vínculo entre profissional e paciente               |
| V5     | Correção do schema de medicação e atendimento       |
| V6     | Alinhamento do schema de atendimento e requisição   |
| V7     | Complementação de profissional e uso de medicação   |

O modelo de `CentroCusto` utiliza **herança com joined-table**, onde `Escola` e `Unidade` estendem a tabela pai por meio de FK, com discriminador `tipo`.

---

## Como Executar

### Pré-requisitos

- Docker e Docker Compose instalados
- Java 21 e Maven 3.9+ (para execução local sem Docker)

### Subindo com Docker Compose

```bash
# 1. Clone o repositório
git clone <url-do-repositorio>
cd clinica-es-main

# 2. Configure as variáveis de ambiente
cp .env.example .env
# Edite o .env com suas configurações

# 3. Suba o banco de dados
docker compose up -d postgres

# 4. Execute a aplicação
./mvnw spring-boot:run
```

### Build e execução completa via Docker

```bash
# Build da imagem
docker build -t clinica-medica-api .

# Execução
docker run -p 8000:8000 --env-file .env clinica-medica-api
```

### Execução local (sem Docker)

```bash
# Garanta que o PostgreSQL esteja rodando localmente
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8000`.

---

## Variáveis de Ambiente

| Variável             | Padrão                              | Descrição                            |
|----------------------|-------------------------------------|--------------------------------------|
| `DB_HOST`            | `localhost`                         | Host do PostgreSQL                   |
| `DB_PORT`            | `5432`                              | Porta do PostgreSQL                  |
| `DB_NAME`            | `clinica`                           | Nome do banco de dados               |
| `DB_USER`            | `postgres`                          | Usuário do banco                     |
| `DB_PASSWORD`        | `123`                               | Senha do banco                       |
| `TOKEN_SECRET`       | `clinica-ucsal-secret-key-2025`     | Chave secreta para assinatura do JWT |
| `PORT`               | `8000`                              | Porta em que a API será exposta      |
| `ALLOWED_ORIGINS`    | `http://localhost:4200`             | Origens permitidas pelo CORS         |
| `ADMIN_USER`         | `admin`                             | Username do administrador padrão     |
| `ADMIN_PASSWORD`     | `admin123`                          | Senha do administrador padrão        |
| `PROFISSIONAL_USER`  | `profissional`                      | Username do profissional padrão      |
| `PROFISSIONAL_PASSWORD` | `profissional123`               | Senha do profissional padrão         |

> **⚠️ Atenção:** Em produção, substitua todos os valores padrão por credenciais seguras e defina `TOKEN_SECRET` com uma chave de no mínimo 32 caracteres.

---

## Usuários Padrão

Na primeira inicialização, o `SystemStart` cria automaticamente dois usuários para facilitar o desenvolvimento e testes:

| Username       | Senha            | Perfil             |
|----------------|------------------|--------------------|
| `admin`        | `admin123`       | ADMINISTRADOR      |
| `profissional` | `profissional123`| PROFISSIONAL_SAUDE |

> As credenciais são configuráveis via variáveis de ambiente antes da primeira execução.
