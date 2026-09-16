# orca-pro

Backend do MVP de orçamentos para prestadores de serviço. Monólito modular em
Spring Boot (Java 17), com autenticação JWT, persistência em JPA e
notificações via eventos internos do Spring (`ApplicationEventPublisher`) —
o mesmo desacoplamento de um broker de mensagens, sem precisar de
infraestrutura externa nesta fase.

## Rodando localmente

Por padrão, a aplicação sobe com um banco H2 em memória — não precisa
instalar nada além do Java 17 e do Maven.

```bash
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`. O console do H2 fica em
`http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:orcapro`, usuário
`sa`, senha em branco).

## Rodando com PostgreSQL (produção)

Defina as variáveis de ambiente antes de subir:

```bash
export DB_URL=jdbc:postgresql://SEU_HOST:5432/orcapro
export DB_USER=orcapro
export DB_PASSWORD=sua_senha
export DB_DRIVER=org.postgresql.Driver
export JWT_SECRET=uma-chave-longa-e-aleatoria-de-producao
```

## Principais endpoints

| Método | Caminho | Autenticado? |
|---|---|---|
| POST | `/api/auth/cadastro` | não |
| POST | `/api/auth/login` | não |
| GET/PUT | `/api/empresa` | sim |
| GET/POST/PUT/DELETE | `/api/clientes` | sim |
| GET/POST/PUT/DELETE | `/api/servicos` | sim |
| GET/POST/PUT/DELETE | `/api/orcamentos` | sim |
| POST | `/api/orcamentos/{id}/enviar` | sim |
| POST | `/api/orcamentos/{id}/reabrir` | sim |
| POST | `/api/orcamentos/{id}/duplicar` | sim |
| GET | `/api/orcamentos/dashboard` | sim |
| GET | `/api/publico/orcamentos/{token}` | não (link do cliente) |
| POST | `/api/publico/orcamentos/{token}/resposta` | não (aprovar/recusar) |

Todas as rotas autenticadas esperam o header `Authorization: Bearer <token>`,
obtido no cadastro ou login. O `empresaId` do token é usado automaticamente
para isolar os dados entre empresas — nunca é preciso (nem possível) passar
esse valor manualmente pela API.

## O que foi simplificado nesta primeira versão

- **Upload de logotipo**: o endpoint `/api/empresa/logotipo` recebe uma URL
  já hospedada, não faz upload de arquivo. Falta decidir onde armazenar
  arquivos (S3, Cloud Storage, etc.) antes de implementar o upload real.
- **Recuperação de senha**: removida por depender de envio de e-mail, que
  ainda não está configurado. O cadastro e login funcionam normalmente.
- **Numeração do orçamento**: usa uma contagem simples por empresa
  (`COUNT + 1`). Funciona bem no volume de um MVP, mas antes de um uso mais
  pesado vale trocar por um contador atômico dedicado, para evitar
  duplicidade em cadastros simultâneos.
- **Notificações**: publicadas como eventos internos do Spring e hoje só
  registradas em log. Trocar por e-mail/push real é só criar um novo
  listener de `OrcamentoEvento` — nenhuma mudança é necessária no módulo de
  orçamentos.
