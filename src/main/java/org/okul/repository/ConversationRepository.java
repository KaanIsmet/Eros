package org.okul.repository;

import org.okul.db.Config;
import org.okul.model.Conversation;

import java.util.List;
import java.util.Optional;

public class ConversationRepository {

    public Conversation save(Conversation conversation) {
        return Config.getInstance().withHandle(handle ->
                handle.createUpdate(
                                "INSERT INTO conversations (title, model_name) VALUES (:title, :modelName)")
                        .bindBean(conversation)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Long.class)
                        .findOne()
                        .map(id -> {
                            conversation.setId(id);
                            return conversation;
                        })
                        .orElseThrow(() -> new RuntimeException("Failed to save conversation"))
        );
    }

    public Optional<Conversation> findById(Long id) {
        return Config.getInstance().withHandle(handle ->
                handle.createQuery(
                                "SELECT * FROM conversations WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(Conversation.class)
                        .findOne()
        );
    }

    public List<Conversation> findAll() {
        return Config.getInstance().withHandle(handle ->
                handle.createQuery(
                                "SELECT * FROM conversations ORDER BY created_at DESC")
                        .mapToBean(Conversation.class)
                        .list()
        );
    }

    public void deleteById(Long id) {
        Config.getInstance().withHandle(handle ->
                handle.createUpdate(
                                "DELETE FROM conversations WHERE id = :id")
                        .bind("id", id)
                        .execute()
        );
    }
}