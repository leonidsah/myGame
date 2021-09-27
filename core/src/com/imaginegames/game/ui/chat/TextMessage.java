package com.imaginegames.game.ui.chat;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class TextMessage extends Widget implements Message {
    private String messageText;
    private String senderName;
    private boolean isAccepted = false;
    private boolean isDenied = false;
    private boolean isRead = false;

    public TextMessage(String messageText, String senderName) {
        this.messageText = messageText;
        this.senderName = senderName;
    }

    @Override
    public void accept() {
        isAccepted = true;
    }

    @Override
    public boolean isAccepted() {
        return isAccepted;
    }

    @Override
    public void deny() {
        isDenied = true;
    }

    @Override
    public boolean isDenied() {
        return isDenied;
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

    @Override
    public void setContext(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public String getSenderName() {
        return senderName;
    }

}
