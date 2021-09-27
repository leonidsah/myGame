package com.imaginegames.game.ui.chat;

import com.badlogic.gdx.ApplicationLogger;

/**
 * An implementation of {@link ApplicationLogger} that sends log messages to {@link ChatLabel} in order to
 * log something using in-game chat
 */
public class ChatLogger implements ApplicationLogger {
    private ChatLabel chatLabel;

    public ChatLogger(ChatLabel chatLabel) {
        this.chatLabel = chatLabel;
    }

    @Override
    public void log(String tag, String message) {
        chatLabel.handle(new TextMessage(message, "log-" + tag));
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        chatLabel.handle(new TextMessage(message, "log-" + tag));
    }

    @Override
    public void error(String tag, String message) {
        chatLabel.handle(new TextMessage(message, "error-" + tag));
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        chatLabel.handle(new TextMessage(message, "error-" + tag));
    }

    @Override
    public void debug(String tag, String message) {
        chatLabel.handle(new TextMessage(message, "debug-" + tag));
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        chatLabel.handle(new TextMessage(message, "debug-" + tag));
    }
}
