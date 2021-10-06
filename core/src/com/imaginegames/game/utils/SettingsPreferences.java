package com.imaginegames.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.imaginegames.game.Values;

public class SettingsPreferences {
    private static Preferences prefs = Gdx.app.getPreferences("settings.mm");

    public static void load() {
        Values.stagesDebug = prefs.getBoolean("stagesDebug", false);
        Values.logFPS = prefs.getBoolean("logFps", false);
    }
    public static void save() {
        prefs.putBoolean("stagesDebug", Values.stagesDebug);
        prefs.putBoolean("logFps", Values.logFPS);
        prefs.flush();
    }
}
