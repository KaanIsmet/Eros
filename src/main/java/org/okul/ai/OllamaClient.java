package org.okul.ai;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;

import java.util.List;

public class OllamaClient {

    private StreamingChatModel model;
    private String currentModel;

    public OllamaClient() {
        String baseUrl = System.getenv("OLLAMA_URL") != null
                ? System.getenv("OLLAMA_URL")
                : "http://localhost:11434";

        this.currentModel = System.getenv("OLLAMA_MODEL") != null
                ? System.getenv("OLLAMA_MODEL")
                : "llama3.2:latest";

        this.model = buildModel(baseUrl, currentModel);
    }

    public void chat(List<ChatMessage> history, StreamingChatResponseHandler handler) {
        model.chat(history, handler);
    }

    public void setModel(String modelName) {
        this.currentModel = modelName;
        String baseUrl = System.getenv("OLLAMA_URL") != null
                ? System.getenv("OLLAMA_URL")
                : "http://localhost:11434";
        this.model = buildModel(baseUrl, modelName);
    }

    public String getCurrentModel() {
        return currentModel;
    }

    private StreamingChatModel buildModel(String baseUrl, String modelName) {
        return OllamaStreamingChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(modelName)
                .build();
    }
}