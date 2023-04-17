package com.imaginegames.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.imaginegames.game.screens.loading.LoadingScreen;
import com.imaginegames.game.utils.SettingsPreferences;

public class MyGame extends Game {
    public AssetManager assets; // Shouldn't be static, as mentioned on AssetManager page on GitHub wiki

    @Override
    public void create() {
        if (Values.loadSettingsFromPrefs) SettingsPreferences.load();
        assets = new AssetManager();
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        this.setScreen(new LoadingScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

}