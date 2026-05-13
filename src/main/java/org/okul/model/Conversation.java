package org.okul.model;

import java.time.LocalDateTime;

public class Conversation {

    private Long id;
    private String title;
    private String modelName;
    private LocalDateTime createdAt;

    public Conversation() {}

    public Conversation(String title, String modelName) {
        this.title = title;
        this.modelName = modelName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) — %s", id, title, modelName, createdAt);
    }
}