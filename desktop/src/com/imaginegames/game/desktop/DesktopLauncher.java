package com.imaginegames.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.imaginegames.game.MilitaryMadnessMain;
import com.imaginegames.game.utils.Values;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 120;
		config.vSyncEnabled = false;
		config.width = Values.SCREEN_WIDTH;
		config.height = Values.SCREEN_HEIGHT;
		config.fullscreen = Values.DESKTOP_FULLSCREEN;
		config.title = "Military Madness" + " " + Values.VERSION;
		config.addIcon("desktop-icon.png", Files.FileType.Internal);
		new LwjglApplication(new MilitaryMadnessMain(), config);
	}
}
