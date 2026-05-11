# Eros 🤖

**Eros** is a personal AI assistant desktop application built in Java, powered by local large language models via [Ollama](https://ollama.com). No cloud. No API keys. Just you and your AI — fully offline.

---

## Features

- 💬 Chat with local LLMs in a clean JavaFX interface
- 🔒 Fully offline — your conversations never leave your machine
- 🔄 Switch between models (Llama 3, Mistral, Gemma, and more)
- ⚡ Streaming responses for a natural, real-time feel
- 🧠 Powered by [LangChain4j](https://github.com/langchain4j/langchain4j) for memory and prompt management

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| UI Framework | JavaFX |
| LLM Runtime | Ollama |
| LLM Client | LangChain4j |
| Build Tool | Maven |

---

## Prerequisites

- **Java 21+** — [Download here](https://adoptium.net)
- **Maven 3.8+** — [Download here](https://maven.apache.org/download.cgi)
- **Ollama** — [Download here](https://ollama.com/download)

---

## Getting Started

### 1. Install and start Ollama

```bash
# Download and install Ollama from https://ollama.com/download
# Then pull a model
ollama pull llama3
```

### 2. Clone the repository

```bash
git clone https://github.com/yourname/eros.git
cd eros
```

### 3. Build the project

```bash
mvn clean install
```

### 4. Run the app

```bash
mvn javafx:run
```

---

## Project Structure

```
eros/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/yourname/eros/
        │       ├── ErosApp.java          # JavaFX entry point
        │       ├── ChatController.java   # UI logic and event handling
        │       └── LlmService.java       # Ollama / LangChain4j integration
        └── resources/
            └── com/yourname/eros/
                └── chat.fxml             # UI layout
```

---

## Configuration

By default, Eros connects to Ollama at `http://localhost:11434` using the `llama3` model. You can change these in `LlmService.java`:

```java
OllamaChatModel model = OllamaChatModel.builder()
    .baseUrl("http://localhost:11434")
    .modelName("llama3") // change to any model you have pulled
    .build();
```

### Supported Models

Any model available via Ollama works with Eros. Some popular options:

| Model | Command |
|---|---|
| Llama 3 | `ollama pull llama3` |
| Mistral | `ollama pull mistral` |
| Gemma | `ollama pull gemma` |
| Phi-3 | `ollama pull phi3` |

---

## Building a Fat JAR

To package Eros as a single executable JAR:

```bash
mvn clean package
java -jar target/eros-1.0-SNAPSHOT.jar
```

---

## Roadmap

- [ ] Model selector dropdown in the UI
- [ ] Conversation history and persistence
- [ ] System prompt / persona customization
- [ ] Dark mode support
- [ ] Export conversations to text or PDF

---

## License

MIT License — see [LICENSE](LICENSE) for details.

---

## Acknowledgements

- [Ollama](https://ollama.com) — local LLM serving made simple
- [LangChain4j](https://github.com/langchain4j/langchain4j) — Java LLM framework
- [OpenJFX](https://openjfx.io) — modern Java UI toolkit
