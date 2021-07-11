package com.imaginegames.game.screens;

import com.badlogic.gdx.Screen;
import com.imaginegames.game.MilitaryMadnessMain;
import com.imaginegames.game.utils.GameProcessor;

public class GameScreen implements Screen {

    public MilitaryMadnessMain game;
    public GameProcessor gameProcessor;

    public GameScreen(MilitaryMadnessMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        gameProcessor = new GameProcessor(this);
    }

    @Override
    public void render(float delta) {
        gameProcessor.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        gameProcessor.resize(width, height);
    }

    @Override
    public void pause() {
        gameProcessor.pause();
    }

    @Override
    public void resume() {
        gameProcessor.resume();
    }

    @Override
    public void hide() {
        gameProcessor.hide();
    }

    @Override
    public void dispose() {
        gameProcessor.dispose();
    }
}