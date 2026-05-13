package org.okul.service;

import org.okul.ai.OllamaClient;

public class ModelService {

    private final OllamaClient ollamaClient;

    public ModelService() {
        this.ollamaClient = new OllamaClient();
    }

    public String getCurrentModel() {
        return ollamaClient.getCurrentModel();
    }

    public void switchModel(String modelName) {
        ollamaClient.setModel(modelName);
        System.out.println("✔ Switched to model: " + modelName);
    }
}
