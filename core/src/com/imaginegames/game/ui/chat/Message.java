package com.imaginegames.game.ui.chat;

public interface Message {
    // Called when a message is received by a MessageHandler
    public void accept();
    public boolean isAccepted();
    // May be called by a parser to stop dealing with message (e.g. if the message is empty or it's a command)
    public void deny();
    public boolean isDenied();
    // Called when a message is handled by a MessageHandler (e.g. shown on a screen)
    public void read();
    public void unread();
    public boolean isRead();
    public Message getMessage();
    public int getLength();
    // MessageType is an enum
    public MessageType getType();
    // Get a String object that describes a message context
    public String getContext();
    public void setContext(String context);
    public String getSenderName();
}
