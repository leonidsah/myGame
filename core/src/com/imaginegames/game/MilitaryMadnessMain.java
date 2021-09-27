package com.imaginegames.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.imaginegames.game.screens.LoadingScreen;

public class MilitaryMadnessMain extends Game {
    public SpriteBatch batch;
    public SpriteBatch interface_batch;
    public AssetManager assets; // Shouldn't be static, as mentioned on AssetManager page on GitHub wiki

    @Override
    public void create() {
        assets = new AssetManager();
        batch = new SpriteBatch();
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