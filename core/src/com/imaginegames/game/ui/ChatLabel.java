                 package com.imaginegames.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;

public class ChatLabel extends Label implements MessageHandler {
    private ArrayList<Message> messageList;
    private String chatString;
    private String defaultSenderName = "leonidsah";

    public ChatLabel(Skin skin, String labelStyleName) {
        super("Напишите что-нибудь", skin, labelStyleName);
        messageList = new ArrayList<>(10);
    }
    @Override
    public void handle(Message message) {
        message.accept();
        if (message.getContext().equals("/clearchat")) {
            clearChat();
        }
        else {
            messageList.add(0, message);
            // Remove last message to remain chat bounded
            if (messageList.size() == 10) messageList.remove(9);
            appendMessagesToChat();
        }
    }

    private void appendMessagesToChat() {
        chatString = "";
        for (Message message : messageList) {
            chatString = chatString.concat("[" + defaultSenderName + "]: " + message.getContext() + "\n");
        }
        setText(chatString);
    }

    public void clearChat() {
        messageList.clear();
        appendMessagesToChat();
    }
}
