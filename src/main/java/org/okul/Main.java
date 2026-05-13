package org.okul;

import org.okul.db.Config;
import org.okul.model.Conversation;
import org.okul.service.ChatService;
import org.okul.service.ModelService;

import java.util.Scanner;
import java.util.List;
import org.okul.model.Message;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String banner = """
            
            ███████╗██████╗  ██████╗ ███████╗
            ██╔════╝██╔══██╗██╔═══██╗██╔════╝
            █████╗  ██████╔╝██║   ██║███████╗
            ██╔══╝  ██╔══██╗██║   ██║╚════██║
            ███████╗██║  ██║╚██████╔╝███████║
            ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝
            
            Your personal local AI assistant.
            """;
    private static final String menu = """
            ┌─────────────────────────────┐
            │         Main Menu           │
            ├─────────────────────────────┤
            │  1. New Chat                │
            │  2. View History            │
            │  3. Switch Model            │
            │  4. Exit                    │
            └─────────────────────────────┘
            Choose an option:\s""";

    private static final ChatService chatService = new ChatService();
    private static final ModelService modelService = new ModelService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println(banner);
        Config.getInstance();
        System.out.println("✔ Connected to database.");
        System.out.println("✔ Ollama ready at " + System.getenv("OLLAMA_URL"));
        System.out.println();

        boolean running = true;

        while (running) {
            System.out.println(menu);
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1" -> startChat();
                case "2" -> viewHistory();
                case "3" -> switchModel();
                case "4" -> {
                    running = false;
                }
                default -> System.out.println("Invalid option. Please choose 1-4.\n");
            }
        }
    }

    private static void startChat() {
        System.out.print("\nEnter a title for this conversation (or press Enter to skip): ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) title = "Untitled";

        Conversation conversation = chatService.startConversation(title);
        System.out.println("\nStarting chat with model: " + conversation.getModelName());
        System.out.println("Type 'exit' to return to the main menu.\n");

        while (true) {
            System.out.print("You: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("\nReturning to main menu...\n");
                break;
            }

            if (input.isEmpty()) continue;

            System.out.print("Eros: ");
            System.out.flush();
            chatService.chat(conversation, input);
            System.out.println();
        }
    }
    private static void viewHistory() {
        List<Conversation> conversations = chatService.getAllConversations();

        if (conversations.isEmpty()) {
            System.out.println("\nNo conversations found.\n");
            return;
        }

        System.out.println("\n--- Conversation History ---");
        for (Conversation c : conversations) {
            System.out.printf("  [%d] %s | model: %s | %s%n",
                    c.getId(), c.getTitle(), c.getModelName(), c.getCreatedAt());
        }

        System.out.print("\nEnter conversation ID to view (or press Enter to go back): ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println();
            return;
        }

        try {
            Long id = Long.parseLong(input);
            List<Message> messages = chatService.getMessages(id);

            if (messages.isEmpty()) {
                System.out.println("\nNo messages in this conversation.\n");
                return;
            }

            System.out.println("\n--- Messages ---");
            for (Message m : messages) {
                System.out.printf("%s: %s%n", m.getRole(), m.getContent());
                System.out.println();
            }

            System.out.print("Delete this conversation? (y/n): ");
            String confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("y")) {
                chatService.deleteConversation(id);
                System.out.println("Conversation deleted.\n");
            } else {
                System.out.println();
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.\n");
        }
    }
    private static void switchModel() {
        System.out.println("\nCurrent model: " + modelService.getCurrentModel());
        System.out.print("Enter new model name (or press Enter to go back): ");

        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println();
            return;
        }

        modelService.switchModel(input);
        System.out.println();
    }
}