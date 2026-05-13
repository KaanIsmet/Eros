package org.okul.repository;

import org.okul.db.Config;
import org.okul.model.Message;

import java.util.List;

public class MessageRepository {

    public Message save(Message message) {
        return Config.getInstance().withHandle(handle ->
                handle.createUpdate(
                                "INSERT INTO messages (conversation_id, role, content) " +
                                        "VALUES (:conversationId, :role, :content)")
                        .bindBean(message)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Long.class)
                        .findOne()
                        .map(id -> {
                            message.setId(id);
                            return message;
                        })
                        .orElseThrow(() -> new RuntimeException("Failed to save message"))
        );
    }

    public List<Message> findByConversationId(Long conversationId) {
        return Config.getInstance().withHandle(handle ->
                handle.createQuery(
                                "SELECT * FROM messages WHERE conversation_id = :conversationId " +
                                        "ORDER BY created_at ASC")
                        .bind("conversationId", conversationId)
                        .mapToBean(Message.class)
                        .list()
        );
    }

    public void deleteByConversationId(Long conversationId) {
        Config.getInstance().withHandle(handle ->
                handle.createUpdate(
                                "DELETE FROM messages WHERE conversation_id = :conversationId")
                        .bind("conversationId", conversationId)
                        .execute()
        );
    }
}