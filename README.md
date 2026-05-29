# Projeto Rodojacto - Sistema de Gerenciamento

Este documento fornece as instruções necessárias para a configuração, compilação e execução do sistema Rodojacto. O projeto consiste em uma plataforma de gerenciamento composta por um back-end em Kotlin, um front-end em Angular e persistência de dados em MySQL.

## 1. Tecnologias Utilizadas

O desenvolvimento do sistema baseou-se nas seguintes tecnologias:

### Back-end
*   **Linguagem:** Kotlin 2.2.21
*   **Framework:** Spring Boot 4.0.6
*   **Segurança:** Spring Security com autenticação baseada em JSON Web Tokens (JWT)
*   **Persistência:** Spring Data JPA / Hibernate
*   **Migrações de Banco de Dados:** Flyway
*   **Gerenciamento de Dependências:** Gradle

### Front-end
*   **Framework:** Angular 21
*   **Linguagem:** TypeScript
*   **Estilização:** CSS (Vanilla)
*   **Gerenciamento de Estado:** Angular Signals para reatividade otimizada

### Infraestrutura e Banco de Dados
*   **Banco de Dados:** MySQL 8.0
*   **Conteinerização:** Docker e Docker Compose

## 2. Pré-requisitos

Para a correta execução do ambiente, é necessária a instalação das seguintes ferramentas:

*   Docker (versão 20.10 ou superior)
*   Docker Compose (versão 2.0 ou superior)
*   (Opcional para execução manual) Java JDK 21 e Node.js 22

## 3. Estrutura de Diretórios

*   `/rodojacto`: Código fonte do serviço back-end (Spring Boot).
*   `/front-end`: Código fonte da interface do usuário (Angular).
*   `docker-compose.yml`: Arquivo de orquestração dos serviços.
*   `.env.example`: Exemplo de configurações de variáveis de ambiente.

## 4. Instruções de Execução via Docker (Recomendado)

O projeto está configurado para ser executado integralmente via Docker Compose, o que automatiza a configuração do banco de dados, compilação dos serviços e provisionamento da rede interna.

### Passos para execução:

1.  Certifique-se de que as portas 80, 8080 e 3306 não estejam sendo utilizadas por outros serviços em sua máquina host.
2.  Crie um arquivo `.env` baseado no `.env.example`.
3.  Navegue até a raiz do projeto no terminal.
4.  Execute o comando abaixo para iniciar o provisionamento:
    ```bash
    docker compose up --build
    ```
5.  Aguarde a conclusão do build e a inicialização dos serviços. O backend aguardará a prontidão do banco de dados antes de iniciar.

### Acesso aos Serviços:
*   **Interface Web (Front-end):** http://localhost (Porta 80)
*   **API (Back-end):** http://localhost:8080
*   **Banco de Dados (MySQL):** localhost:3306

## 5. Configuração de Segurança (JWT)

O sistema utiliza a chave secreta definida abaixo para a assinatura e validação de tokens JWT:
`dvgAvrK49yCXE1ioUupY9S6Ag971cG1GHsOC3alMrwjfzDIyba9RO1vwb4z6ecP6`

Esta chave está configurada no arquivo `docker-compose.yml` e é injetada no ambiente do container back-end.

## 6. Credenciais de Banco de Dados

As credenciais padrão sugeridas para o arquivo `.env` são:
*   **Database:** rodojacto_db
*   **User:** admin
*   **Password:** rodo1234jacto

## 7. Carga Inicial de Dados (Seeding)

O sistema possui um mecanismo de **Seed Automático** (`DataSeeder`) que popula a base de dados na primeira inicialização.

**Configurações do Seeding:**
- Execução transacional (@Transactional) para garantir consistência.
- Logs detalhados visíveis via `docker logs rodojacto_backend`.
- Popula: 5 Organizações, Colaboradores (Gerentes e Operadores) e Dispositivos.

## 8. Credenciais para Teste

### Perfil Gerente (Acesso Global)
*   **E-mail:** admin@rodojacto.com.br
*   **Senha:** 123456

### Perfil Operador (Acesso Restrito)
*   **E-mail:** carlos@rodojacto.com.br (Gerente da Filial Sul) ou e-mails de operadores como `op.matriz0@rodojacto.com.br`.
*   **Senha:** 123456

> **Nota sobre Segurança:** Usuários com perfil `OPERATOR` possuem visualização restrita apenas à organização a que pertencem, com interface adaptada para ocultar ações administrativas.
