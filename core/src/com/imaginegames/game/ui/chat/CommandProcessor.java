package com.imaginegames.game.ui.chat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Null;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CommandProcessor {
    private HashMap<String, Command> commands = new HashMap<>();
    private Character commandSymbol = '/';
    private ChatLabel chatLabel;
    private String argv[] = null;

    public CommandProcessor(ChatLabel chatLabel) {
        this.chatLabel = chatLabel;
    }

    // Commands can be added from different places where ChatLabel is present
    public void addCommand(Command command) {
        commands.put(command.getName().toLowerCase(), command);
    }

    public Command getCommand(String commandName) {
        return commands.get(commandName.toLowerCase());
    }

    public Set<String> getCommandNames() {
        return commands.keySet();
    }

    // Before using this method, it's supposed that string has already went through method like removeExcessWhitespaces() in ChatLabel
    @Null
    public Command parseCommand(String string) {
        Command command = commands.get("notacommand");
        if (!string.matches(commandSymbol + "(.*)")) {
            return null;
        }
        // Remove the whitespace after '/' if it is
        if (string.matches("^(/ )(.*)")) string = string.replaceFirst( commandSymbol + " ", commandSymbol.toString());
        String[] commandParts = string.split("[ ]");
        for (String cmdName : commands.keySet()) {
            if (commandParts[0].equalsIgnoreCase(commandSymbol + cmdName)) {
                command = commands.get(cmdName);
                break;
            }
        }
        if (command.isUsingArgs()) {
            argv = new String[commandParts.length - 1];
            if (commandParts.length - 1 >= 1) {
                for (int i = 1; i < commandParts.length; i++) {
                    argv[i - 1] = commandParts[i];
                }
            }
        }

        return command;
    }

    public boolean executeCommand(Command command) {
        if (command != null && command.isUsingArgs()) {
            command.execute(argv);
            argv = null;
            return true;
        }
        else if (command != null) {
            command.execute();
            return true;
        }
        else return false;
    }
}
