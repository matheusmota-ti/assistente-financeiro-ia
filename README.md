# Assistente Financeiro com IA — Spring Boot + Spring AI

API REST para registrar receitas e despesas, consultar saldo e usar IA para interpretar comandos em linguagem natural. Projeto educacional inspirado no desafio Spring AI da DIO.

## Funcionalidades
- Cadastro, listagem, consulta e exclusão de transações.
- Validação de descrição, categoria, tipo e valor positivo.
- Relatório com receitas, despesas, saldo e despesas por categoria.
- Assistente com Spring AI `ChatClient` e Tool Calling para consultar e registrar transações.
- Persistência local com H2.

## Tecnologias
Java 21, Spring Boot 3.5, Spring AI 1.1, Spring Web, Spring Data JPA, H2 e Gradle.

## Pré-requisitos
- JDK 21.
- Chave de API da OpenAI para o endpoint de IA.
- Gradle instalado ou wrapper Gradle adicionado à raiz do projeto.

## Como executar
1. Configure a variável de ambiente `OPENAI_API_KEY` com sua chave. Não coloque a chave no código nem a envie ao GitHub.
2. Na raiz, execute `gradle bootRun` (ou `./gradlew bootRun`, se adicionar o wrapper).
3. A API estará em `http://localhost:8080`.

Sem uma chave da OpenAI, os endpoints CRUD e relatórios funcionam; o endpoint do assistente precisa de credenciais.

## Endpoints

### Criar transação
`POST /api/transacoes`
```json
{
  "tipo": "DESPESA",
  "descricao": "Almoço",
  "categoria": "Alimentação",
  "valor": 32.90,
  "data": "2026-10-10"
}
```
`tipo` aceita `RECEITA` ou `DESPESA`. A data é opcional.

### Listar transações
`GET /api/transacoes`

### Consultar uma transação
`GET /api/transacoes/1`

### Excluir transação
`DELETE /api/transacoes/1`

### Resumo financeiro
`GET /api/relatorios/resumo`

### Relatório por categoria — melhoria implementada
`GET /api/relatorios/categoria/Alimentação`

### Conversar com a IA
`POST /api/assistente/perguntar`
```json
{ "mensagem": "Registre uma despesa de 25 reais com café na categoria alimentação." }
```
A IA usa ferramentas Java reais para consultar e registrar dados. Revise os dados registrados antes de usar a aplicação para decisões financeiras.

## Testes rápidos com curl
```bash
curl -X POST http://localhost:8080/api/transacoes \
  -H "Content-Type: application/json" \
  -d '{"tipo":"RECEITA","descricao":"Salário","categoria":"Trabalho","valor":2500}'
curl http://localhost:8080/api/relatorios/resumo
curl http://localhost:8080/api/relatorios/categoria/Alimentação
```

## Arquitetura
- `domain`: entidade e tipos de transação.
- `repository`: persistência com Spring Data JPA.
- `service`: regras de negócio e validações.
- `api`: endpoints REST e tratamento de erros.
- `ai`: `ChatClient` e ferramentas disponíveis para Tool Calling.

## Melhoria autoral
Implementei relatório de despesas agrupadas por categoria e consulta específica por categoria. Isso ajuda a responder perguntas como “quanto gastei com alimentação?” e pode ser acessado diretamente pela API ou pela ferramenta de IA.

## Limitação conhecida
Esta versão implementa comandos de texto com Tool Calling. A etapa de áudio (upload, transcrição e resposta em voz) não está incluída ainda; ela pode ser adicionada usando os modelos de transcrição e Text-to-Speech do Spring AI. Assim, não declare o fluxo de voz como concluído na entrega.

## Referência
- Desafio DIO: https://github.com/digitalinnovationone/dio-spring-boot-learning-track/tree/main/05-spring-ai
- Spring AI: https://docs.spring.io/spring-ai/reference/
