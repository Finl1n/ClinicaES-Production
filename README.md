# 🏥 ClinicalES — Sistema de Controle de Clínica Médica

Sistema completo para gerenciamento de uma clínica médica acadêmica, permitindo o controle de pacientes, profissionais de saúde, atendimentos, prontuários e medicações, com integração total entre frontend, backend e banco de dados.

---

## 🚀 Tecnologias Utilizadas

### 🔹 Frontend
- Angular
- TypeScript
- HTML5
- SCSS
- Reactive Forms
- Componentes Standalone

### 🔹 Backend
- Java 17+
- Spring Boot
- Spring Data JPA
- Hibernate

### 🔹 Banco de Dados
- PostgreSQL

---

## 📦 Funcionalidades

### 👤 Usuários
- Cadastro de administradores e profissionais de saúde
- Controle de acesso por perfil
- Completar cadastro do profissional

### 🏫 Estrutura Organizacional
- Cadastro de escolas
- Cadastro de unidades
- Associação com IES

### 🧑‍⚕️ Pacientes
- Cadastro de pacientes
- Edição de informações
- Inativação de pacientes
- Filtro por categoria

### 💊 Medicações
- Cadastro de medicações
- Controle de estoque
- Controle de validade
- Atualização de quantidade via edição
- Ativação/Inativação
- Indicadores visuais:
  - ⚠️ Estoque baixo
  - ❌ Sem estoque
  - 🔴 Medicação vencida

### 🩺 Atendimentos
- Criação de atendimentos
- Registro de sintomas
- Registro de diagnóstico
- Registro de tratamento
- Associação de medicação ao paciente
- Baixa automática de estoque
- Registro do uso de medicação no banco

### 📋 Prontuário
- Histórico completo do paciente
- Exibição de atendimentos
- Exibição de diagnósticos
- Exibição de tratamentos
- Exibição de medicações utilizadas
- Integração direta com o banco de dados

### 📊 Relatórios
- Medicações vencidas
- Medicações com estoque baixo
- Profissionais com cadastro incompleto
- Requisições de medicação

---

## 🔗 Integração Front + Back

O sistema segue arquitetura desacoplada:

- Frontend consome API REST
- Backend fornece endpoints estruturados
- Comunicação via JSON (HTTP)

---

## ⚙️ Como Rodar o Projeto

### Backend

```bash
cd back
mvn clean install
mvn spring-boot:run
