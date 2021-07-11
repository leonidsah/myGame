package com.imaginegames.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.imaginegames.game.MilitaryMadnessMain;
import com.imaginegames.game.screens.GameScreen;
import com.imaginegames.game.ui.UIProcessor;

public class GameProcessor {
    private MilitaryMadnessMain game;
    private GameScreen gameScreen;
    private OrthographicCamera cam;
    private SpriteBatch batch;
    private FillViewport fillViewport;
    private UIProcessor uiProcessor;
    private float aspectRatio;
    private Texture bg;

    public GameProcessor(GameScreen gameScreen) {
        this.game = gameScreen.game;
        this.gameScreen = gameScreen;
        init();
    }
    public void init() {
        // Camera part
        aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        cam = new OrthographicCamera();
        fillViewport = new FillViewport(30, 30 * aspectRatio, cam);
        fillViewport.apply();
        cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0);
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        // Assets
        bg = game.assets.get("textures/background1.png", Texture.class);
        // Controllers part
        uiProcessor = new UIProcessor(this);
        uiProcessor.init();
    }
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Update camera
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        // Drawing world
        game.batch.begin();
        game.batch.draw(bg, 0, 0, 30, 30 * aspectRatio);
        game.batch.end();
        uiProcessor.render(delta);
    }
    public void resize(int width, int height) {
        //fillViewport.update(width, height);
        uiProcessor.resize(width, height);
    }
    public void pause() {

    }
    public void resume() {

    }
    public void hide() {

    }
    public void dispose() {
        uiProcessor.dispose();
    }
    public FillViewport getViewport() {
        return fillViewport;
    }
    public OrthographicCamera getCamera() {
        return cam;
    }
    public MilitaryMadnessMain getGame() {
        return game;
    }
    public GameScreen getGameScreen() {
        return gameScreen;
    }
    /*public SpriteBatch getBatch() {
        return batch;
    }*/
}
