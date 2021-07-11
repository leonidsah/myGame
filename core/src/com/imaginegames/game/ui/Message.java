package com.imaginegames.game.ui;

public interface Message {
    // Called when a message is received by a MessageHandler
    public void accept();
    public void deny();
    public boolean isAccepted();
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
    //public String getSenderName();
}
