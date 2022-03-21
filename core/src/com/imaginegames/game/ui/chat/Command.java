package com.imaginegames.game.ui.chat;

import com.badlogic.gdx.utils.Null;

public class Command {
    private String name;
    private boolean useArgs;

    public Command(String commandName) {
        this(commandName, false);
    }

    public Command(String commandName, boolean useArgs) {
        this.name = commandName;
        this.useArgs = useArgs;
    }

    // Should be overwritten in anonymous class
    public void execute() { };

    public void execute(String[] argv) {
        execute();
    };

    public final String getName() {
        return name;
    }
    public final boolean isUsingArgs() {
        return useArgs;
    }
}