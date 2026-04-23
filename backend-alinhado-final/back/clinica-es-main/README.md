<div align="center">

# 🏥 Clínica Médica UCSAL — Backend API

**Sistema de Controle de Atendimento da Clínica Médica**
Universidade Católica do Salvador — Análise e Desenvolvimento de Sistemas

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6.x-6DB33F?style=for-the-badge&logo=springsecurity)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Flyway](https://img.shields.io/badge/Flyway-10.x-CC0200?style=for-the-badge&logo=flyway)](https://flywaydb.org/)

</div>

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Stack Tecnológico](#-stack-tecnológico)
- [Arquitetura](#-arquitetura)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Execução](#-instalação-e-execução)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)
- [Banco de Dados](#-banco-de-dados)
- [Autenticação](#-autenticação)
- [Endpoints da API](#-endpoints-da-api)
- [Regras de Negócio](#-regras-de-negócio)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Progresso das Sprints](#-progresso-das-sprints)
- [Equipe](#-equipe)

---

## 🎯 Sobre o Projeto

Backend RESTful para o sistema de controle de atendimento da clínica médica da UCSAL. O sistema gerencia o ciclo completo de atendimento clínico institucional, incluindo profissionais de saúde, pacientes, prontuários, medicações e relatórios administrativos.

### Funcionalidades principais

- 🔐 **Autenticação JWT** com controle de acesso por perfil (Administrador e Profissional de Saúde)
- 🏫 **Gestão de Escolas e Unidades** — centros de custo institucionais
- 👨‍⚕️ **Cadastro de Profissionais** com fluxo em duas etapas
- 🧑‍🤝‍🧑 **Gestão de Pacientes** com criação automática de prontuário
- 📋 **Registro de Atendimentos** vinculados ao prontuário do paciente
- 💊 **Controle de Medicações** com baixa automática de estoque
- 📊 **Relatórios Administrativos** consolidados

---

## 🛠 Stack Tecnológico

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 21 (LTS) | Linguagem principal |
| Spring Boot | 3.x | Framework principal |
| Spring Security | 6.x | Autenticação e autorização |
| Spring Data JPA | 3.x | Acesso ao banco de dados |
| PostgreSQL | 15+ | Banco de dados relacional |
| Flyway | 10.x | Versionamento do schema |
| Lombok | 1.18+ | Redução de boilerplate |
| Java JWT (Auth0) | 4.4.0 | Geração e validação de tokens |
| Bean Validation | 3.x | Validação de DTOs |
| Maven | 3.9+ | Gerenciamento de dependências |

---

## 🏗 Arquitetura

O projeto segue o padrão **arquitetura em camadas (Layered Architecture)**:

```
src/main/java/com/ucsal/clinica/
├── config/
│   └── security/           # SecurityConfig, SecurityFilter, TokenService, UsuarioAutenticado
├── controller/             # Camada HTTP — recebe e responde requisições REST
├── service/                # Camada de negócio — regras e orquestração
├── repository/             # Interfaces JPA — acesso ao banco de dados
├── domain/                 # Entidades JPA
│   └── enums/              # Status, RoleUsuario, CategoriaPaciente, TipoAtendimento, TipoRequisicao
├── dto/
│   ├── request/            # DTOs de entrada (Records imutáveis)
│   └── response/           # DTOs de saída com factory method from()
│       └── relatorio/      # DTOs específicos para relatórios
└── exception/              # NegocioException e GlobalExceptionHandler
```

### Fluxo de uma requisição autenticada

```
Cliente HTTP
    │
    ▼
SecurityFilter ──── valida token JWT ──── extrai username ──── registra no SecurityContext
    │
    ▼
Controller ──── @PreAuthorize (verifica role) ──── valida DTO com @Valid
    │
    ▼
Service ──── aplica regras de negócio ──── lança NegocioException se violada
    │
    ▼
Repository ──── executa query JPA/JPQL ──── PostgreSQL
    │
    ▼
Response JSON ◄──── DTO.from(entidade) ◄──── entidade salva/retornada
```

---

## ✅ Pré-requisitos

Antes de começar, garanta que você tem instalado:

- [Java 21 JDK](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [PostgreSQL 15+](https://www.postgresql.org/download/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) (recomendado) ou outra IDE Java

---

## 🚀 Instalação e Execução

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/clinica-ucsal-backend.git
cd clinica-ucsal-backend
```

### 2. Crie o banco de dados

Acesse o PostgreSQL e execute:

```sql
CREATE DATABASE clinica_ucsal;
```

### 3. Configure as credenciais

Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/clinica_ucsal
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

> **Dica:** Em vez de editar o arquivo, prefira usar variáveis de ambiente (ver seção [Variáveis de Ambiente](#-variáveis-de-ambiente)).

### 4. Execute a aplicação

```bash
mvn spring-boot:run
```

O Flyway executará as migrations automaticamente. A API estará disponível em:

```
http://localhost:8080
```

### 5. Verifique se está funcionando

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

Resposta esperada:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

## 🔧 Variáveis de Ambiente

Em produção, **nunca** use credenciais hardcoded. Configure as variáveis abaixo:

| Variável | Descrição | Padrão (dev) |
|---|---|---|
| `SPRING_DATASOURCE_URL` | URL de conexão com o banco | `jdbc:postgresql://localhost:5432/clinica_ucsal` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | `postgres` |
| `API_SECURITY_TOKEN_SECRET` | Segredo para assinar tokens JWT | `clinica-ucsal-secret-key-2025` |
| `API_SECURITY_TOKEN_EXPIRATION` | Expiração do token em ms | `7200000` (2 horas) |
| `SERVER_PORT` | Porta da aplicação | `8080` |

Exemplo de execução com variáveis de ambiente:

```bash
export SPRING_DATASOURCE_PASSWORD=minha_senha_segura
export API_SECURITY_TOKEN_SECRET=meu_segredo_jwt_seguro
mvn spring-boot:run
```

---

## 🗄 Banco de Dados

### Migrações Flyway

O Flyway gerencia o versionamento do schema automaticamente. Os scripts ficam em `src/main/resources/db/migration/`:

| Arquivo | Descrição |
|---|---|
| `V1__criar_tabelas_base.sql` | Criação de todas as tabelas |
| `V2__inserir_usuario_admin.sql` | Usuário administrador padrão |
| `V3__adicionar_cadastro_completo_profissional.sql` | Coluna `cadastro_completo` |

### Modelo de dados

```
centro_custo (abstract)
├── escola          (id, coordenador)
└── unidade         (id, responsavel)

usuario             (id, username, password_hash, role)
profissional_saude  (id, nome, especialidade, conselho, numero_registro, cadastro_completo, status, usuario_id)

paciente            (id, nome, categoria, email, telefone, status)
prontuario          (id, paciente_id)  ← criado automaticamente com o paciente
atendimento         (id, data_inicio, data_fim, sintomas, diagnostico, tratamento, tipo, prontuario_id, profissional_saude_id)

medicacao           (id, nome, descricao, estoque, validade, status)
item_atendimento    (id, quantidade, dosagem, atendimento_id, medicacao_id)
requisicao_medicacao (id, tipo, medicacao_id)
```

### Usuário padrão (criado pela migration V2)

| Campo | Valor |
|---|---|
| Username | `admin` |
| Senha | `admin123` |
| Role | `ADMINISTRADOR` |

> ⚠️ **Troque a senha do admin em produção.**

---

## 🔐 Autenticação

A API usa **JWT (JSON Web Token)** com autenticação stateless.

### Como autenticar

**1. Faça login para obter o token:**

```http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**2. Use o token em todas as requisições protegidas:**

```http
GET /pacientes
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Perfis de acesso

| Role | Acesso |
|---|---|
| `ADMINISTRADOR` | Gestão de escolas, unidades, profissionais, medicações e relatórios |
| `PROFISSIONAL_SAUDE` | Cadastro de pacientes, atendimentos, prontuários e uso de medicações |

### Respostas de erro de autenticação

```json
// 401 — credenciais inválidas
{ "erro": "Credenciais inválidas" }

// 403 — sem permissão para o recurso
```

---

## 📡 Endpoints da API

> **Base URL:** `http://localhost:8080`
> Todos os endpoints (exceto `/auth/login`) exigem o header `Authorization: Bearer <token>`.

### 🔑 Autenticação

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `POST` | `/auth/login` | Autenticar e obter token JWT | Público |

---

### 🏫 Escolas

| Método | Endpoint | Descrição | Role |
|---|---|---|---|
| `POST` | `/escolas` | Cadastrar escola | ADMIN |
| `GET` | `/escolas` | Listar escolas ativas | ADMIN |
| `GET` | `/escolas/{id}` | Buscar escola por ID | ADMIN |
| `PUT` | `/escolas/{id}` | Atualizar escola | ADMIN |
| `PATCH` | `/escolas/{id}/inativar` | Inativar escola | ADMIN |

<details>
<summary>Exemplo de request/response</summary>

**POST /escolas**
```json
// Request
{
  "nome": "Escola de Saúde",
  "ies": "UCSAL",
  "coordenador": "Prof. Dr. João Silva"
}

// Response 201
{
  "id": 1,
  "nome": "Escola de Saúde",
  "ies": "UCSAL",
  "coordenador": "Prof. Dr. João Silva",
  "status": "ATIVO"
}
```
</details>

---

### 🏢 Unidades

| Método | Endpoint | Descrição | Role |
|---|---|---|---|
| `POST` | `/unidades` | Cadastrar unidade | ADMIN |
| `GET` | `/unidades` | Listar unidades ativas | ADMIN |
| `GET` | `/unidades/{id}` | Buscar unidade por ID | ADMIN |
| `PUT` | `/unidades/{id}` | Atualizar unidade | ADMIN |
| `PATCH` | `/unidades/{id}/inativar` | Inativar unidade | ADMIN |

---

### 👨‍⚕️ Profissionais de Saúde

| Método | Endpoint | Descrição | Role |
|---|---|---|---|
| `POST` | `/profissionais` | Cadastrar profissional (etapa 1) | ADMIN |
| `GET` | `/profissionais` | Listar profissionais ativos | ADMIN |
| `GET` | `/profissionais/{id}` | Buscar por ID | ADMIN |
| `PATCH` | `/profissionais/{id}/inativar` | Inativar profissional | ADMIN |
| `GET` | `/profissionais/meu-perfil` | Consultar próprio perfil | PROFISSIONAL |
| `PATCH` | `/profissionais/meu-perfil/complemento` | Completar cadastro (etapa 2) | PROFISSIONAL |

<details>
<summary>Fluxo de cadastro em duas etapas</summary>

**Etapa 1 — Admin cadastra o profissional:**
```json
// POST /profissionais
{
  "nome": "Dra. Maria Santos",
  "username": "maria.santos",
  "password": "senha123"
}
```

**Etapa 2 — Profissional complementa após login:**
```json
// PATCH /profissionais/meu-perfil/complemento
{
  "especialidade": "Clínica Geral",
  "conselho": "CRM",
  "numeroRegistro": "12345-BA"
}
```
</details>

---

### 🧑‍🤝‍🧑 Pacientes

| Método | Endpoint | Descrição | Role |
|---|---|---|---|
| `POST` | `/pacientes` | Cadastrar paciente + prontuário automático | PROFISSIONAL |
| `GET` | `/pacientes` | Listar pacientes ativos | AMBOS |
| `GET` | `/pacientes/{id}` | Buscar paciente por ID | AMBOS |
| `PUT` | `/pacientes/{id}` | Atualizar paciente | PROFISSIONAL |
| `PATCH` | `/pacientes/{id}/inativar` | Inativar paciente | PROFISSIONAL |

<details>
<summary>Categorias de paciente disponíveis</summary>

- `ALUNO`
- `COLABORADOR_UNIDADE`
- `COLABORADOR_ESCOLA`
- `EXTERNO`

</details>

---

### 📋 Prontuários

| Método | Endpoint | Descrição | Role |
|---|---|---|---|
| `GET` | `/prontuarios` | Listar todos os prontuários | AMBOS |
| `GET` | `/prontuarios/paciente/{id}` | Prontuário completo com atendimentos | AMBOS |

---

### 🩺 Atendimentos

| Método | Endpoint | Descrição | Role |
|---|---|---|---|
| `POST` | `/atendimentos` | Registrar novo atendimento | PROFISSIONAL |
| `PATCH` | `/atendimentos/{id}/encerrar` | Encerrar atendimento | PROFISSIONAL |
| `GET` | `/atendimentos/{id}` | Buscar atendimento por ID | AMBOS |
| `GET` | `/atendimentos/meus` | Listar atendimentos do profissional | PROFISSIONAL |
| `GET` | `/atendimentos/paciente/{id}` | Listar atendimentos de um paciente | AMBOS |
| `POST` | `/atendimentos/{id}/medicacoes` | Adicionar medicação ao atendimento | PROFISSIONAL |

<details>
<summary>Tipos de atendimento disponíveis</summary>

- `URGENCIA`
- `EMERGENCIA`
- `CONSULTA`
- `REVISAO`

</details>

---

### 💊 Medicações

| Método | Endpoint | Descrição | Role |
|---|---|---|---|
| `POST` | `/medicacoes` | Cadastrar medicação | ADMIN |
| `GET` | `/medicacoes` | Listar medicações ativas | ADMIN |
| `GET` | `/medicacoes/disponiveis` | Listar disponíveis para uso clínico | PROFISSIONAL |
| `GET` | `/medicacoes/{id}` | Buscar por ID | AMBOS |
| `PUT` | `/medicacoes/{id}` | Atualizar medicação | ADMIN |
| `PATCH` | `/medicacoes/{id}/inativar` | Inativar medicação | ADMIN |

---

### 📦 Requisições de Medicação

| Método | Endpoint | Descrição | Role |
|---|---|---|---|
| `POST` | `/requisicoes-medicacao` | Solicitar reposição | PROFISSIONAL |
| `GET` | `/requisicoes-medicacao` | Listar requisições (`?tipo=URGENTE`) | ADMIN |
| `DELETE` | `/requisicoes-medicacao/{id}` | Remover requisição atendida | ADMIN |

<details>
<summary>Tipos de requisição disponíveis</summary>

- `URGENTE`
- `CRITICO`
- `PREVENTIVO`

</details>

---

### 📊 Relatórios

> Todos os endpoints de relatório são exclusivos do perfil **ADMINISTRADOR**.

| Método | Endpoint | Query Params | Descrição |
|---|---|---|---|
| `GET` | `/relatorios/atendimentos` | `inicio`, `fim`, `tipo` (opcional) | Atendimentos por período |
| `GET` | `/relatorios/estoque-baixo` | `limiar` (opcional, padrão: 10) | Medicações com estoque baixo |
| `GET` | `/relatorios/requisicoes-pendentes` | — | Requisições abertas por prioridade |
| `GET` | `/relatorios/cadastros-incompletos` | — | Profissionais com cadastro incompleto |
| `GET` | `/relatorios/consolidado` | `inicio`, `fim` | Dashboard com todos os totais |

<details>
<summary>Exemplo de uso dos relatórios</summary>

```http
# Atendimentos de urgência em março de 2025
GET /relatorios/atendimentos?inicio=2025-03-01T00:00:00&fim=2025-03-31T23:59:59&tipo=URGENCIA

# Medicações com menos de 5 unidades em estoque
GET /relatorios/estoque-baixo?limiar=5

# Dashboard do mês de março
GET /relatorios/consolidado?inicio=2025-03-01T00:00:00&fim=2025-03-31T23:59:59
```

**Resposta do consolidado:**
```json
{
  "totalAtendimentosNoPeriodo": 47,
  "totalMedicacoesEstoqueBaixo": 3,
  "requisicoesPorTipo": {
    "URGENTE": 2,
    "CRITICO": 1,
    "PREVENTIVO": 0
  },
  "totalProfissionaisComCadastroIncompleto": 4
}
```
</details>

---

## 📏 Regras de Negócio

| ID | Regra | Impacto |
|---|---|---|
| RN001 | Escolas não podem ser excluídas, apenas inativadas | `PATCH /escolas/{id}/inativar` |
| RN002 | Unidades não podem ser excluídas, apenas inativadas | `PATCH /unidades/{id}/inativar` |
| RN003 | Cada escola tem nome único por IES | Validado no cadastro |
| RN004 | Cada unidade tem nome único por IES | Validado no cadastro |
| RN005 | Profissionais não podem ser excluídos, apenas inativados | `PATCH /profissionais/{id}/inativar` |
| RN006 | Pacientes não podem ser excluídos, apenas inativados | `PATCH /pacientes/{id}/inativar` |
| RN007 | Todo paciente deve possuir um prontuário associado | Criado automaticamente no cadastro |
| RN008 | O prontuário não pode existir sem paciente | FK `NOT NULL` + criação transacional |
| RN009 | Medicações não podem ser excluídas, apenas inativadas | `PATCH /medicacoes/{id}/inativar` |
| RN010 | Medicação usada deve estar ativa, dentro da validade e com estoque suficiente | Validado antes de registrar uso |
| RN011 | Toda utilização de medicação reduz o estoque automaticamente | Executado na mesma transação |
| RN012 | Horário de encerramento deve ser posterior ao de início | Validado em `registrar()` e `encerrar()` |
| RN013 | Profissional só pode atender após completar o cadastro | Validado em `registrar()` |

---

## 📁 Estrutura do Projeto

```
clinica-ucsal-backend/
│
├── src/
│   ├── main/
│   │   ├── java/com/ucsal/clinica/
│   │   │   ├── config/
│   │   │   │   └── security/
│   │   │   │       ├── SecurityConfig.java
│   │   │   │       ├── SecurityFilter.java
│   │   │   │       ├── TokenService.java
│   │   │   │       └── UsuarioAutenticado.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── EscolaController.java
│   │   │   │   ├── UnidadeController.java
│   │   │   │   ├── ProfissionalSaudeController.java
│   │   │   │   ├── PacienteController.java
│   │   │   │   ├── ProntuarioController.java
│   │   │   │   ├── AtendimentoController.java
│   │   │   │   ├── AtendimentoMedicacaoController.java
│   │   │   │   ├── MedicacaoController.java
│   │   │   │   ├── RequisicaoMedicacaoController.java
│   │   │   │   └── RelatorioController.java
│   │   │   ├── domain/
│   │   │   │   ├── enums/
│   │   │   │   │   ├── Status.java
│   │   │   │   │   ├── RoleUsuario.java
│   │   │   │   │   ├── CategoriaPaciente.java
│   │   │   │   │   ├── TipoAtendimento.java
│   │   │   │   │   └── TipoRequisicao.java
│   │   │   │   ├── CentroCusto.java
│   │   │   │   ├── Escola.java
│   │   │   │   ├── Unidade.java
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── ProfissionalSaude.java
│   │   │   │   ├── Paciente.java
│   │   │   │   ├── Prontuario.java
│   │   │   │   ├── Atendimento.java
│   │   │   │   ├── ItemAtendimento.java
│   │   │   │   ├── Medicacao.java
│   │   │   │   └── RequisicaoMedicacao.java
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── EscolaRequest.java
│   │   │   │   │   ├── UnidadeRequest.java
│   │   │   │   │   ├── ProfissionalSaudeRequest.java
│   │   │   │   │   ├── ProfissionalSaudeComplementoRequest.java
│   │   │   │   │   ├── PacienteRequest.java
│   │   │   │   │   ├── AtendimentoRequest.java
│   │   │   │   │   ├── EncerrarAtendimentoRequest.java
│   │   │   │   │   ├── ItemAtendimentoRequest.java
│   │   │   │   │   ├── MedicacaoRequest.java
│   │   │   │   │   └── RequisicaoMedicacaoRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── relatorio/
│   │   │   │       │   ├── RelatorioAtendimentoResponse.java
│   │   │   │       │   ├── RelatorioEstoqueResponse.java
│   │   │   │       │   ├── RelatorioRequisicaoResponse.java
│   │   │   │       │   ├── RelatorioCadastroIncompletoResponse.java
│   │   │   │       │   └── RelatorioConsolidadoResponse.java
│   │   │   │       ├── LoginResponse.java
│   │   │   │       ├── EscolaResponse.java
│   │   │   │       ├── UnidadeResponse.java
│   │   │   │       ├── ProfissionalSaudeResponse.java
│   │   │   │       ├── PacienteResponse.java
│   │   │   │       ├── ProntuarioResumoResponse.java
│   │   │   │       ├── ProntuarioDetalheResponse.java
│   │   │   │       ├── AtendimentoResponse.java
│   │   │   │       ├── AtendimentoResumoResponse.java
│   │   │   │       ├── ItemAtendimentoResponse.java
│   │   │   │       ├── MedicacaoResponse.java
│   │   │   │       └── RequisicaoMedicacaoResponse.java
│   │   │   ├── exception/
│   │   │   │   ├── NegocioException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── repository/
│   │   │   │   ├── UsuarioRepository.java
│   │   │   │   ├── EscolaRepository.java
│   │   │   │   ├── UnidadeRepository.java
│   │   │   │   ├── ProfissionalSaudeRepository.java
│   │   │   │   ├── PacienteRepository.java
│   │   │   │   ├── ProntuarioRepository.java
│   │   │   │   ├── AtendimentoRepository.java
│   │   │   │   ├── ItemAtendimentoRepository.java
│   │   │   │   ├── MedicacaoRepository.java
│   │   │   │   └── RequisicaoMedicacaoRepository.java
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── EscolaService.java
│   │   │   │   ├── UnidadeService.java
│   │   │   │   ├── ProfissionalSaudeService.java
│   │   │   │   ├── PacienteService.java
│   │   │   │   ├── ProntuarioService.java
│   │   │   │   ├── AtendimentoService.java
│   │   │   │   ├── AtendimentoMedicacaoService.java
│   │   │   │   ├── MedicacaoService.java
│   │   │   │   ├── RequisicaoMedicacaoService.java
│   │   │   │   └── RelatorioService.java
│   │   │   └── ClinicaApplication.java
│   │   └── resources/
│   │       ├── db/migration/
│   │       │   ├── V1__criar_tabelas_base.sql
│   │       │   ├── V2__inserir_usuario_admin.sql
│   │       │   └── V3__adicionar_cadastro_completo_profissional.sql
│   │       └── application.properties
│   └── test/
│       └── java/com/ucsal/clinica/
│
├── .gitignore
├── pom.xml
└── README.md
```

---

## 📅 Progresso das Sprints

| Sprint | Escopo | Status | Endpoints |
|---|---|---|---|
| Sprint 0 | Fundação (estrutura, deps, migrations) | ✅ Concluído | — |
| Sprint 1 | Autenticação JWT | ✅ Concluído | 1 |
| Sprint 2 | CRUD Escola e Unidade | ✅ Concluído | 10 |
| Sprint 3 | Profissional de Saúde | ✅ Concluído | 6 |
| Sprint 4 | Paciente e Prontuário | ✅ Concluído | 7 |
| Sprint 5 | Atendimentos | 🔄 Em andamento | 5 |
| Sprint 6 | Medicações e Estoque | ⏳ Pendente | 10 |
| Sprint 7 | Relatórios Administrativos | ⏳ Pendente | 5 |

**Total:** 24 de 44 endpoints implementados *(Sprints 0–4 concluídas)*

---

## 🤝 Equipe

Projeto desenvolvido por uma equipe de 5 pessoas como atividade acadêmica do curso de Análise e Desenvolvimento de Sistemas da UCSAL — Salvador, BA.

---

## 📄 Convenções do Projeto

- **Commits:** `feat(modulo): descricao curta` — ex: `feat(auth): implementa autenticação JWT`
- **Branches:** `feature/nome-da-feature` — ex: `feature/sprint-5-atendimentos`
- **Inativação:** Nenhuma entidade é excluída fisicamente — sempre usar `PATCH /{id}/inativar`
- **DTOs:** Usar Java Records para todos os DTOs de request e response
- **Injeção:** Sempre via construtor — nunca `@Autowired` em campos
- **Transações:** `@Transactional` em escrita, `@Transactional(readOnly = true)` em consultas

---

<div align="center">

**UCSAL — Universidade Católica do Salvador**
Análise e Desenvolvimento de Sistemas · 2025

</div>