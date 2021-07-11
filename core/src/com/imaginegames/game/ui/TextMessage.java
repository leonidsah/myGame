package com.imaginegames.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class TextMessage extends Widget implements Message {
    private String messageText;
    private boolean isAccepted = false;
    private boolean isDenied = false;
    private boolean isRead = false;

    public TextMessage(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public void accept() {
        isAccepted = true;
    }

    @Override
    public void deny() {
        isDenied = true;
        messageText = "<deleted message>";
    }

    @Override
    public boolean isAccepted() {
        return isAccepted;
    }

    @Override
    public void read() {
        isRead = true;
    }

    @Override
    public void unread() {
        isRead = false;
    }

    @Override
    public boolean isRead() {
        return isRead;
    }

    @Override
    public Message getMessage() {
        return this;
    }

    @Override
    public int getLength() {
        if (messageText != null) return messageText.length();
        else return -1;
    }

    @Override
    public MessageType getType() {
        return MessageType.TEXT;
    }

    @Override
    public String getContext() {
        return messageText;
    }
}
