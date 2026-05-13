package org.okul.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import org.okul.ai.OllamaClient;
import org.okul.model.Conversation;
import org.okul.model.Message;
import org.okul.model.Role;
import org.okul.repository.ConversationRepository;
import org.okul.repository.MessageRepository;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class ChatService {

    private final OllamaClient ollamaClient;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public ChatService() {
        this.ollamaClient = new OllamaClient();
        this.conversationRepository = new ConversationRepository();
        this.messageRepository = new MessageRepository();
    }

    public Conversation startConversation(String title) {
        String modelName = System.getenv("OLLAMA_MODEL") != null
                ? System.getenv("OLLAMA_MODEL")
                : "llama3";
        Conversation conversation = new Conversation(title, modelName);
        return conversationRepository.save(conversation);
    }

    public void chat(Conversation conversation, String userInput) {
        Message userMessage = new Message(conversation.getId(), Role.USER, userInput);
        messageRepository.save(userMessage);

        List<ChatMessage> history = loadHistory(conversation.getId());
        StringBuilder responseBuffer = new StringBuilder();
        CountDownLatch latch = new CountDownLatch(1);

        ollamaClient.chat(history, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String token) {
                System.out.print(token);
                System.out.flush();
                responseBuffer.append(token);
            }

            @Override
            public void onCompleteResponse(ChatResponse response) {
                messageRepository.save(new Message(
                        conversation.getId(),
                        Role.ASSISTANT,
                        responseBuffer.toString()
                ));
                latch.countDown();  // ← signal done
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("\nError communicating with Ollama: " + error.getMessage());
                latch.countDown();  // ← signal done even on error
            }
        });

        try {
            latch.await();  // ← wait here until response is complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private List<ChatMessage> loadHistory(Long conversationId) {
        return messageRepository.findByConversationId(conversationId)
                .stream()
                .map(msg -> switch (msg.getRole()) {
                    case USER -> UserMessage.from(msg.getContent());
                    case ASSISTANT -> AiMessage.from(msg.getContent());
                })
                .collect(Collectors.toList());
    }

    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    public List<Message> getMessages(Long conversationId) {
        return messageRepository.findByConversationId(conversationId);
    }

    public void deleteConversation(Long conversationId) {
        messageRepository.deleteByConversationId(conversationId);
        conversationRepository.deleteById(conversationId);
    }
}