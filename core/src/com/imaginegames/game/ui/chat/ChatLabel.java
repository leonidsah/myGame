package com.imaginegames.game.ui.chat;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.imaginegames.game.ui.chat.messages.Message;
import com.imaginegames.game.ui.chat.messages.MessageHandler;
import com.imaginegames.game.ui.chat.messages.MessageType;
import com.imaginegames.game.ui.chat.messages.TextMessage;

import java.util.ArrayList;

public class ChatLabel extends Label implements MessageHandler {
    private ArrayList<Message> messageList;
    private String chatString;
    private CommandProcessor commandProcessor;

    public ChatLabel(LabelStyle labelStyle) {
        super("", labelStyle);
        messageList = new ArrayList<>(10);
        commandProcessor = new CommandProcessor(this);
        commandProcessor.addCommand(new Command("notacommand") {
            @Override
            public void execute() {
                handleConsoleMessage("[RED]Command not found. See [YELLOW]/commands[][]");
            }
        });
        commandProcessor.addCommand(new Command("help") {
            @Override
            public void execute() {
                handleConsoleMessage("Welcome to the chat. See [YELLOW]/commands[] for available commands");
            }
        });
        commandProcessor.addCommand(new Command("clearchat") {
            @Override
            public void execute() {
                clearChat();
            }
        });
        commandProcessor.addCommand(new Command("commands") {
            @Override
            public void execute() {
                String commandsList = "";
                for (String cmdName : commandProcessor.getCommandNames()) {
                    if (!cmdName.equalsIgnoreCase("notacommand")) commandsList += cmdName + " ";
                }
                handleConsoleMessage("Available commands: [YELLOW]" + commandsList + "[]");
            }
        });
        commandProcessor.executeCommand(commandProcessor.getCommand("help"));
    }

    @Override
    public void handle(Message message) {
        if (message == null) throw new IllegalArgumentException("Message cannot be null");
        message.accept();
        if (message.getType() == MessageType.TEXT) parse(message);
        if (!message.isDenied()) {
            messageList.add(0, message);
            // Remove last message to remain chat bounded
            if (messageList.size() == 10) messageList.remove(9);
            appendMessagesToChat();
        }
    }

    // Handle a TextMessage that was sent not by user and, therefore, doesn't need to be parsed. Works faster than handle()
    public void handleConsoleMessage(String message) {
        TextMessage textMessage = new TextMessage(message, "console");
        messageList.add(0, textMessage);
        // Remove last message to remain chat bounded
        if (messageList.size() == 10) messageList.remove(9);
        appendMessagesToChat();
    }

    private void appendMessagesToChat() {
        chatString = "";
        for (Message message : messageList) {
            if (!message.getSenderName().equalsIgnoreCase("console")) {
                chatString = chatString.concat("[LIGHT_GRAY][[" + message.getSenderName() + "]: " + message.getContext() + "[]\n");
            } else chatString = chatString.concat("> " + message.getContext() + "\n");
        }
        setText(chatString);
    }

    public void clearChat() {
        messageList.clear();
        appendMessagesToChat();
    }

    private void parse(Message message) {
        removeExcessWhitespaces(message);
        if (message.getContext().equals("")) {
            message.deny();
            return;
        }
        if (commandProcessor.executeCommand(commandProcessor.parseCommand(message.getContext()))) message.deny();
        escapeColorMarkup(message);
    }

    private void removeExcessWhitespaces(Message message) {
        String messageText = message.getContext();
        // Deleting whitespaces at beginning of the message
        if (messageText.matches("^(\\s+)(.*)")) {
            messageText = messageText.replaceFirst("\\s+", "");
        }
        // Replacing all repeated whitespaces
        messageText = messageText.replaceAll("\\s+", " ");
        message.setContext(messageText);
    }

    private void escapeColorMarkup(Message message) {
        message.setContext(message.getContext().replaceAll("\\[", "[["));
    }

    public CommandProcessor getCommandProcessor() {
        return commandProcessor;
    }

    public Message getPreviousMessage() {
        if (messageList.size() > 1) return messageList.get(0);
        else return null;
    }
}