# Plataforma de Gerenciamento de Estágios e Oportunidades de Carreira

## :octocat: Integrantes
* [Pedro Henrique Matos Oliveira](https://github.com/Pedro-Matos19)
* [Kévna Tenório Brito Cavalcanti](https://github.com/kevna2329)
* [Antônio Carlos Batista Vaz](https://github.com/AntonioCVaz)
* [João Henrique Araújo de Souza](https://github.com/jota-aga)
* [José Uilton Ferreira de Siqueira](https://github.com/joseuilton)

## :page_with_curl: Sobre o Projeto
Projeto para desenvolvimento de um software Web completo (Frontend e Backend) para a disciplina de __Engenharia de Software__, ministrada pela Professora **Thaís Alves Burity Rocha**, na Universidade Federal do Agreste de Pernambuco (UFAPE). O projeto visa a avaliação da 2ª Verificação de Aprendizagem.

O sistema consiste em uma **Plataforma de Gerenciamento de Estágios**, que tem como objetivo conectar discentes, empresas e a instituição de ensino. A plataforma permitirá que empresas divulguem vagas, alunos se candidatem a oportunidades e a instituição gerencie os contratos e documentos de estágio de forma centralizada e eficiente.

## :round_pushpin: Objetivos
O objetivo principal é aplicar os conhecimentos de desenvolvimento colaborativo e arquitetura de software. Funcionalmente, o sistema visa:
* Facilitar o cadastro de empresas e a divulgação de oportunidades de estágio e emprego.
* Permitir que discentes cadastrem seus currículos e se apliquem às vagas.
* Otimizar o acompanhamento dos processos seletivos e a gestão de documentos de estágio.

## :hammer_and_wrench: Tecnologias Usadas
O projeto está estruturado em dois diretórios principais (`/backend` e `/frontend`), utilizando:

### Backend
* **Java** (Linguagem)
* **Spring Boot** (Framework)
* **Spring Data JPA** (Persistência)
* **PostgreSQL** (Banco de Dados)

### Frontend
* **Angular** (Framework Web)
* **TypeScript**
* **HTML/CSS**

## 🚧 Status do Projeto

### ✅ Iteração 1: Infraestrutura (Concluída)
- [x] Configuração do ambiente Java e Spring Boot.
- [x] Configuração do banco de dados PostgreSQL.
- [x] Inicialização do projeto Frontend com Angular.
- [x] Criação dos reposRio e versionamento inicial.

### ✅ Iteração 2: Autenticação e Segurança (Concluída)
**Backend (Finalizado):**
- [x] Implementação do Spring Security e JWT.
- [x] Criação da entidade Usuário e perfis (Admin/User).
- [x] Endpoints de Login e Registro.

**Frontend (Finalizado):**
- [x] Desenvolvimento da tela de Login.
- [x] Integração com a API.

### 🔄 Iteração 3: Gerenciamento de Vagas (Em andamento)
**Backend (Finalizado):**
- [x] Criação da entidade Vaga, DTOs e Enumerações (Localização, Tipo de Vaga).
- [x] Restrição de segurança (Apenas empresas criam/editam vagas).
- [x] Endpoints CRUD de Vagas (`/api/vagas`).
- [x] Testes unitários do serviço de vagas.

**Frontend (Em andamento):**
- [ ] Serviço Angular (`JobsService`) com injeção de Token JWT.
- [ ] Tela de Listagem de Vagas disponíveis.
- [ ] Tela de Criação de Vagas com validação de formulário.