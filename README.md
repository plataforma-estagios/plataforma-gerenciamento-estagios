# Plataforma de Gerenciamento de Estágios e Oportunidades de Carreira

## :octocat: Integrantes

- [Pedro Henrique Matos Oliveira](https://github.com/Pedro-Matos19)
- [Kévna Tenório Brito Cavalcanti](https://github.com/kevna2329)
- [Antônio Carlos Batista Vaz](https://github.com/AntonioCVaz)
- [João Henrique Araújo de Souza](https://github.com/jota-aga)
- [José Uilton Ferreira de Siqueira](https://github.com/joseuilton)

## 📃 Sobre o Projeto

Projeto para desenvolvimento de um software Web completo (Frontend e Backend) para a disciplina de **Engenharia de Software**, ministrada pela Professora **Thaís Alves Burity Rocha**, na Universidade Federal do Agreste de Pernambuco (UFAPE). O projeto visa a avaliação da 2ª Verificação de Aprendizagem.

O sistema consiste em uma **Plataforma de Gerenciamento de Estágios**, que tem como objetivo conectar discentes, empresas e a instituição de ensino. A plataforma permitirá que empresas divulguem vagas, alunos se candidatem a oportunidades e a instituição gerencie os contratos e documentos de estágio de forma centralizada e eficiente.

## 📍 Objetivos

O objetivo principal é aplicar os conhecimentos de desenvolvimento colaborativo e arquitetura de software. Funcionalmente, o sistema visa:

- Facilitar o cadastro de empresas e a divulgação de oportunidades de estágio e emprego.
- Permitir que discentes cadastrem seus currículos e se apliquem às vagas.
- Otimizar o acompanhamento dos processos seletivos e a gestão de documentos de estágio.

## 🚀 Aplicação em Produção

*(URLs de implantação da Quarta Iteração)*

- **Frontend (Aplicação Web):** [Link do Render aqui]
- **Backend (API Rest):** [Link do Render aqui]

## 🛠 Tecnologias Usadas

O projeto está estruturado em dois diretórios principais (`/backend` e `/frontend`), utilizando:

### Backend

- **Java** (Linguagem)
- **Spring Boot** (Framework)
- **Spring Data JPA** (Persistência)
- **PostgreSQL** (Banco de Dados)
- **Docker** (Containerização)
- **JaCoCo & SonarCloud** (Cobertura e Qualidade de Código)

### Frontend

- **Angular** (Framework Web)
- **TypeScript**
- **HTML/CSS**

## 🚧 Status do Projeto

### ✅ Iteração 1: Infraestrutura (Concluída)

- [X] Configuração do ambiente Java e Spring Boot.
- [X] Configuração do banco de dados PostgreSQL.
- [X] Inicialização do projeto Frontend com Angular.
- [X] Criação dos repositórios e versionamento inicial.

### ✅ Iteração 2: Autenticação e Segurança (Concluída)

**Backend (Finalizado):**
- [X] Implementação do Spring Security e JWT.
- [X] Criação da entidade Usuário e perfis (Admin/User).
- [X] Endpoints de Login e Registro.

**Frontend (Finalizado):**
- [X] Desenvolvimento da tela de Login.
- [X] Integração com a API.

### ✅ Iteração 3: Gerenciamento de Vagas (Concluída)

**Backend (Finalizado):**
- [X] Criação da entidade Vaga, DTOs e Enumerações (Localização, Tipo de Vaga).
- [X] Restrição de segurança (Apenas empresas criam/editam vagas).
- [X] Endpoints CRUD de Vagas (`/api/vagas`).
- [X] Testes unitários do serviço de vagas e implementação de filtros dinâmicos.

**Frontend (Finalizado):**
- [X] Serviço Angular (`JobsService`) com injeção de Token JWT.
- [X] Tela de Listagem de Vagas disponíveis.
- [X] Tela de Criação de Vagas com validação de formulário.
- [X] Testes unitários e integração nos principais services.

### 🔄 Iteração 4: Qualidade, CI/CD e Implantação (Em Andamento)

**Integração Contínua (CI) e Qualidade:**
- [X] Configuração do ambiente de testes no Backend (`application-test.properties`).
- [X] Configuração do JaCoCo no Backend para relatórios de cobertura de código.
- [ ] Configuração do pipeline no GitHub Actions para o Backend (Build, Testes, JaCoCo e SonarCloud).
- [ ] Configuração do pipeline no GitHub Actions para o Frontend (Build e Testes).
- [ ] Integração do Backend com a versão gratuita do SonarCloud.
- [ ] Refatoração de código para atender aos critérios de qualidade (0 bugs de segurança, 0 code smells críticos, < 20% de duplicação).

**Implantação Contínua (CD) e Release:**
- [x] Containerização da aplicação com Docker.
- [ ] Implantação automatizada do Backend no Render.
- [ ] Implantação automatizada do Frontend no Render.
- [ ] Publicação do Release final da iteração no repositório.
