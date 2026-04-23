🏥 ClinicalES - Sistema de Controle de Clínica Médica

Sistema completo para gerenciamento de uma clínica médica acadêmica (IES), permitindo o controle de pacientes, profissionais de saúde, atendimentos, prontuários e medicações, com integração total entre frontend, backend e banco de dados.

🚀 Tecnologias Utilizadas
🔹 Frontend
Angular
TypeScript
HTML5 + SCSS
Reactive Forms
Componentes Standalone
🔹 Backend
Java 17+
Spring Boot
Spring Data JPA
Hibernate
🔹 Banco de Dados
PostgreSQL
📦 Funcionalidades
👤 Usuários
Cadastro de administradores e profissionais de saúde
Controle de acesso por perfil
Completar cadastro do profissional
🏫 Estrutura Organizacional
Cadastro de escolas
Cadastro de unidades
Associação com IES
🧑‍⚕️ Pacientes
Cadastro de pacientes
Edição de informações
Inativação de pacientes
Filtro por categoria
💊 Medicações
Cadastro de medicações
Controle de estoque
Controle de validade
Atualização de quantidade via edição
Ativação/Inativação
Indicadores visuais:
⚠️ Estoque baixo
❌ Sem estoque
🔴 Vencida
🩺 Atendimentos
Criação de atendimentos
Registro de:
Sintomas
Diagnóstico
Tratamento
Associação de medicação ao paciente
Baixa automática de estoque
Registro do uso de medicação no banco
📋 Prontuário
Histórico completo do paciente
Exibição de:
Atendimentos
Diagnósticos
Tratamentos
Medicações utilizadas
Integração direta com o banco de dados
📊 Relatórios
Medicações vencidas
Medicações com estoque baixo
Profissionais com cadastro incompleto
Requisições de medicação
🔗 Integração Front + Back

O sistema segue arquitetura desacoplada:

Frontend consome API REST
Backend fornece endpoints estruturados
Comunicação via JSON (HTTP)
⚙️ Como Rodar o Projeto
🔹 Backend (Spring Boot)
cd back
mvn clean install
mvn spring-boot:run

Servidor rodando em:

http://localhost:8080
🔹 Frontend (Angular)
cd front
npm install
ng serve

Acesse:

http://localhost:4200
🗄️ Configuração do Banco de Dados
🔹 PostgreSQL

Crie o banco:

CREATE DATABASE clinica;
🔹 application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/clinica
spring.datasource.username=postgres
spring.datasource.password=senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
📁 Estrutura do Projeto
📦 clinicales
 ┣ 📂 front
 ┃ ┣ 📂 src
 ┃ ┃ ┣ 📂 app
 ┃ ┃ ┃ ┣ 📂 components
 ┃ ┃ ┃ ┣ 📂 services
 ┃ ┃ ┃ ┗ 📂 models
 ┃ ┗ 📂 assets
 ┣ 📂 back
 ┃ ┣ 📂 controller
 ┃ ┣ 📂 service
 ┃ ┣ 📂 repository
 ┃ ┣ 📂 model
 ┃ ┗ 📂 dto
🔥 Principais Regras de Negócio
Medicação não pode ser duplicada por nome
Estoque é controlado via edição
Uso de medicação reduz automaticamente o estoque
Prontuário exibe histórico completo do paciente
Profissionais precisam completar cadastro
Unidades não podem ser excluídas (apenas inativadas)
🎯 Objetivo do Projeto

Projeto desenvolvido com foco acadêmico e profissional, simulando um sistema real de clínica médica com:

Arquitetura moderna (Front + Back + DB)
Regras de negócio reais
Integração completa
Interface responsiva e funcional
👨‍💻 Autor

Gustavo Martins dos Santos

💼 Desenvolvedor Full Stack
📍 Salvador - BA
🔗 GitHub: https://github.com/Finl1n
🔗 LinkedIn: https://www.linkedin.com/in/gustavo-martins-872373155/
⭐ Diferenciais
Sistema completo funcional
Integração real entre módulos
Controle automático de estoque
Prontuário dinâmico
Interface moderna
Código organizado em camadas
📌 Status do Projeto

✔️ Sistema funcional
✔️ Integração completa
✔️ Pronto para portfólio
✔️ Pronto para apresentação
