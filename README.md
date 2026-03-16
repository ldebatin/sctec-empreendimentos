# SCTEC Empreendimentos — API

API REST para cadastro e gerenciamento de empreendimentos do estado de Santa Catarina, desenvolvida como parte do desafio **IA para DEVs — SCTEC**.

---

## Sumário

- [Stack](#stack)
- [Como executar](#como-executar)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Banco de dados](#banco-de-dados)
- [Endpoints](#endpoints)
- [Testes](#testes)
- [Documentação](#documentação)
- [Coleção Bruno](#coleção-bruno)

---

## Stack

- **Java 21** + **Spring Boot 3.4.3**
- **Spring Data JPA** + **Hibernate** (dialeto SQLite)
- **SQLite** — banco de dados embarcado, sem necessidade de servidor
- **Flyway** — migrações versionadas de schema e seed
- **Spring Security** — configurado para permitir acesso público aos endpoints
- **Spring Validation** — validação de DTOs com Bean Validation
- **Springdoc OpenAPI 2.8.5** — documentação interativa via Swagger UI
- **Lombok** — redução de boilerplate
- **JUnit 5 + Mockito** — testes unitários e de camada web

---

## Como executar

### Pré-requisitos

- Java 21+
- Maven 3.8+

### Passos

```bash
# 1. Clone o repositório
git clone <url-do-repositorio>
cd sctec-empreendimentos

# 2. Crie o diretório do banco de dados
mkdir -p data

# 3. Inicie a aplicação
mvn spring-boot:run
```

A aplicação sobe na porta **8080**. As migrações Flyway criam automaticamente a tabela e os 3 registros de seed na primeira execução.

### Perfis disponíveis

| Perfil | Banco | Flyway | DDL |
|--------|-------|--------|-----|
| `default` (produção) | `./data/sctec-empreendimentos.db` | habilitado | `none` |
| `dev` | `./data/sctec-empreendimentos-dev.db` | desabilitado | `create-drop` |

Para rodar no perfil de desenvolvimento:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## Estrutura do projeto

```
src/
└── main/
    ├── java/com/sctec/empreendimentos/
    │   ├── config/
    │   │   ├── OpenApiConfig.java          # Metadados do Swagger
    │   │   └── SecurityConfig.java         # Spring Security (permitAll)
    │   ├── controller/
    │   │   └── EmpreendimentoController.java
    │   ├── dto/
    │   │   ├── EmpreendimentoRequest.java  # Entrada com validações
    │   │   └── EmpreendimentoResponse.java # Saída
    │   ├── exception/
    │   │   └── GlobalExceptionHandler.java # Tratamento centralizado (400/404/500)
    │   ├── model/
    │   │   ├── Empreendimento.java         # Entidade JPA
    │   │   ├── Segmento.java               # Enum de segmento
    │   │   └── Status.java                 # Enum de status
    │   ├── repository/
    │   │   └── EmpreendimentoRepository.java
    │   └── service/
    │       └── EmpreendimentoService.java
    └── resources/
        ├── application.yml
        ├── application-dev.yml
        └── db/migration/
            ├── V1_20260312000000_init.sql
            ├── V2_20260315000000_create_empreendimento.sql
            └── V3_20260315000001_seed_empreendimento.sql

bruno/                                      # Coleção de testes da API
├── bruno.json
├── environments/local.bru
├── listar-empreendimentos.bru
├── buscar-empreendimento-por-id.bru
├── criar-empreendimento.bru
├── atualizar-empreendimento.bru
└── deletar-empreendimento.bru
```

---

## Banco de dados

O projeto usa **SQLite** com banco de dados em arquivo local. O schema é gerenciado pelo **Flyway** com as seguintes migrações:

| Versão | Arquivo | Descrição |
|--------|---------|-----------|
| V1 | `V1_20260312000000_init.sql` | Schema inicial |
| V2 | `V2_20260315000000_create_empreendimento.sql` | Criação da tabela `empreendimento` |
| V3 | `V3_20260315000001_seed_empreendimento.sql` | 3 registros de exemplo |

### Schema da tabela

```sql
CREATE TABLE empreendimento (
    id                INTEGER      PRIMARY KEY AUTOINCREMENT,
    nome              VARCHAR(255) NOT NULL,
    nome_empreendedor VARCHAR(255) NOT NULL,
    municipio         VARCHAR(255) NOT NULL,
    segmento          VARCHAR(50)  NOT NULL,  -- TECNOLOGIA | COMERCIO | INDUSTRIA | SERVICOS | AGRONEGOCIO
    email             VARCHAR(255) NOT NULL,
    telefone          VARCHAR(20)  NOT NULL,
    status            VARCHAR(10)  NOT NULL DEFAULT 'ATIVO'  -- ATIVO | INATIVO
);
```

---

## Endpoints

Base URL: `http://localhost:8080/api/empreendimentos`

| Método | Rota | Descrição | Status de sucesso |
|--------|------|-----------|-------------------|
| `GET` | `/` | Lista todos os empreendimentos | `200 OK` |
| `GET` | `/{id}` | Busca pelo ID | `200 OK` |
| `POST` | `/` | Cria um novo empreendimento | `201 Created` |
| `PUT` | `/{id}` | Atualiza um empreendimento | `200 OK` |
| `DELETE` | `/{id}` | Remove um empreendimento | `204 No Content` |

### Payload de entrada (POST / PUT)

```json
{
  "nome": "TechCatarinense",
  "nomeEmpreendedor": "Ana Paula Silva",
  "municipio": "Florianópolis",
  "segmento": "TECNOLOGIA",
  "email": "ana@techcatarinense.com.br",
  "telefone": "(48) 99999-1001",
  "status": "ATIVO"
}
```

Valores aceitos para `segmento`: `TECNOLOGIA` `COMERCIO` `INDUSTRIA` `SERVICOS` `AGRONEGOCIO`

Valores aceitos para `status`: `ATIVO` `INATIVO`

### Payload de resposta

```json
{
  "id": 1,
  "nome": "TechCatarinense",
  "nomeEmpreendedor": "Ana Paula Silva",
  "municipio": "Florianópolis",
  "segmento": "TECNOLOGIA",
  "email": "ana@techcatarinense.com.br",
  "telefone": "(48) 99999-1001",
  "status": "ATIVO"
}
```

### Erros

| Situação | Status | Corpo |
|----------|--------|-------|
| ID não encontrado | `404 Not Found` | `{ "status": 404, "message": "Empreendimento não encontrado com id: 99", "timestamp": "..." }` |
| Campo inválido | `400 Bad Request` | `{ "status": 400, "message": "Erro de validação", "errors": { "email": "E-mail inválido" } }` |

---

## Testes

O projeto conta com **21 testes** distribuídos em duas classes:

```
EmpreendimentoServiceTest    →  9 testes unitários (Mockito)
EmpreendimentoControllerTest → 11 testes de camada web (MockMvc)
SctecEmpreendimentosApplicationTests →  1 teste de contexto
```

Para executar:

```bash
mvn test
```

Saída esperada:

```
Tests run: 21, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## Documentação

Com a aplicação em execução, a documentação interativa está disponível em:

| URL | Descrição |
|-----|-----------|
| `http://localhost:8080/api/doc` | Swagger UI |
| `http://localhost:8080/api/doc/openapi` | Contrato OpenAPI (JSON) |

---

## Coleção Bruno

A pasta `bruno/` contém uma coleção para o cliente [Bruno](https://www.usebruno.com/), com os 5 endpoints pré-configurados.

**Como usar:**

1. Abra o Bruno
2. Clique em **Open Collection** e selecione a pasta `bruno/`
3. Selecione o environment **local**
4. Execute as requisições

> A variável `baseUrl` aponta para `http://localhost:8080` por padrão.

---

## Link do vídeo

Segue o link da apresentação:

> [YouTube](https://youtu.be/8zXXucKdR0Y)