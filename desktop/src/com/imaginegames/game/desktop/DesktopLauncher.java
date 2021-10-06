package com.imaginegames.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.imaginegames.game.MilitaryMadnessMain;
import com.imaginegames.game.Values;

public class DesktopLauncher {
	public static void main (String[] arg) {
		// Packing textures (during development only)
		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;
		settings.silent = true;
		//TexturePacker.process(settings,"android/assets/textures/raw", "android/assets/textures/textures", "pack");
		// Setting up the LWJGL application
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 120;
		config.vSyncEnabled = false;
		config.width = Values.SCREEN_WIDTH;
		config.height = Values.SCREEN_HEIGHT;
		config.fullscreen = Values.fullscreenMode;
		config.title = "Military Madness" + " " + Values.VERSION + " | " + Values.WELCOME_TITLE;
		config.addIcon("desktop-icon.png", Files.FileType.Internal);
		new LwjglApplication(new MilitaryMadnessMain(), config);
	}
}
