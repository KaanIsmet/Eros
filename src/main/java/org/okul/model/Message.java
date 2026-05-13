package org.okul.model;

import java.time.LocalDateTime;

public class Message {

    private Long id;
    private Long conversationId;
    private Role role;
    private String content;
    private LocalDateTime createdAt;

    public Message() {}

    public Message(Long conversationId, Role role, String content) {
        this.conversationId = conversationId;
        this.role = role;
        this.content = content;
        createdAt = LocalDateTime.now();
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", role, content);
    }
}
