# 🎬 CineHub — Backend

API REST para gerenciamento de um catálogo de séries, desenvolvida com Java e Spring Boot. Integra as APIs OMDb, TMDB e MyMemory para buscar, traduzir e persistir informações de séries e episódios.

---

## 🛠️ Tecnologias

- Java 23
- Spring Boot 3.1
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven
- Jackson
- OMDb API
- TMDB API
- MyMemory Translation API

---

## 📁 Estrutura do projeto

```
src/main/java/br/com/cinehub/
├── controller/
│   └── SerieController.java       # Endpoints REST
├── service/
│   ├── SerieService.java          # Lógica de negócio
│   ├── ConsumoApi.java            # Cliente HTTP
│   ├── ConverteDados.java         # Deserialização JSON
│   └── translate/
│       ├── ConsultaMyMemory.java  # Tradução de sinopses
│       └── tmdb/
│           └── TmdbService.java   # Integração TMDB
├── repository/
│   └── SerieRepository.java       # Acesso ao banco
├── model/
│   ├── Serie.java                 # Entidade JPA
│   ├── Episodio.java              # Entidade JPA
│   ├── Categoria.java             # Enum de gêneros
│   ├── DadosSerie.java            # Record OMDb
│   ├── DadosEpisodio.java         # Record OMDb
│   └── DadosTemporada.java        # Record OMDb
├── dto/
│   ├── SerieDTO.java              # DTO de resposta
│   └── EpisodioDTO.java           # DTO de resposta
└── config/
    └── CorsConfig.java            # Configuração CORS
```

---

## ⚙️ Configuração

### Pré-requisitos

- Java 23
- Maven
- PostgreSQL

### Variáveis de ambiente

Configure as seguintes variáveis antes de rodar:

```bash
OMDB_API_KEY=sua_chave_omdb
TMDB_API_KEY=sua_chave_tmdb
```

### `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cinehub
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

### Como rodar

```bash
# Clone o repositório
git clone https://github.com/Lev1C1/cinehub.git

# Entre na pasta
cd cinehub

# Rode com Maven
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

---

## 📡 Endpoints

### Séries

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/series` | Lista todas as séries |
| GET | `/series/top5` | Top 5 por avaliação |
| GET | `/series/busca?titulo=` | Busca por título |
| GET | `/series/ator?nome=&avaliacao=` | Busca por ator e avaliação mínima |
| GET | `/series/categoria?genero=` | Busca por gênero |
| GET | `/series/filtro?temporadas=&avaliacao=` | Filtro por temporadas e avaliação |
| POST | `/series?nome=` | Busca na OMDb e salva no banco |
| POST | `/series/buscar-completo?nome=` | Busca, salva e carrega todos os episódios |

### Episódios

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/series/episodios/todos?nome=` | Todos os episódios de uma série |
| GET | `/series/episodios/top?nome=` | Top 5 episódios de uma série |
| GET | `/series/episodios/busca?trecho=` | Busca episódio por trecho do título |
| POST | `/series/episodios?nome=` | Carrega episódios de uma série salva |

---

## 🎭 Categorias disponíveis

`Ação` `Animação` `Aventura` `Biografia` `Comédia` `Crime` `Documentário` `Drama` `Esporte` `Família` `Fantasia` `Faroeste` `Ficção Científica` `Guerra` `História` `Mistério` `Musical` `Romance` `Terror` `Thriller`

---

## 🔗 Repositório do Frontend

[github.com/Lev1C1/front-cinehub](https://github.com/Lev1C1/front-cinehub)
