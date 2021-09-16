package com.imaginegames.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.imaginegames.game.MilitaryMadnessMain;
import com.imaginegames.game.Values;

import static java.lang.Math.round;

public class LoadingScreen implements Screen {

    private MilitaryMadnessMain game;
    private Skin skin;
    private Stage stage;
    private ScreenViewport screenViewport;
    private Table rootTable, bottomTable, centerTable, topTable;
    private ProgressBar progressBar;
    private ProgressBar.ProgressBarStyle progressBarStyle;
    private Label percentageLabel, loadingTimeLabel, versionLabel;
    private Image logoImage;
    private float progress = 0f;
    private float loadingTime;
    private boolean setUpProgressBar = true, setUpLogo = true;
    private String[] assetsArray;
    private Pixmap shadowPixmap;
    private Texture shadow;
    private int shadowHeight = 256;

    public LoadingScreen(MilitaryMadnessMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Setting up the stage and tables
        stage = new Stage();
        rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);
        stage.setDebugAll(Values.stagesDebug);
        screenViewport = new ScreenViewport();
        stage.setViewport(screenViewport);
        topTable = new Table();
        centerTable = new Table();
        bottomTable = new Table();
        shadowPixmap = new Pixmap(1, shadowHeight, Pixmap.Format.RGBA8888);
        int color = 0xFFFFFF00;
        float alpha = 0xFF;
        for (int i = 0; i < shadowHeight; i++) {
            alpha -= 255f / shadowHeight;
            shadowPixmap.drawPixel(0, 255 - i, color + (int) alpha);
        }
        shadow = new Texture(shadowPixmap);
        // Assets loading
        assetsArray = new String[]
                {
                        // Skins
                        Values.SKIN_PATH,

                        // Textures
                        "textures/loadingScreenLogo.png",
                        "textures/pack.atlas",

                        // Sounds
                        "sounds/go_sound.mp3",
                        "sounds/cancel_sound.mp3",

                        // Music
                        "music/Wind, winter and cold.mp3",

                        // Tiled maps
                        "tiledMaps/tiledMap.tmx"
                };

        // Loading assets (including assets for loading screen)
        game.assets.load("textures/loadingScreenLogo.png", Texture.class);
        game.assets.load("textures/pack.atlas", TextureAtlas.class);
        for (String asset : assetsArray) {
            if (asset.contains("skins/")) game.assets.load(asset, Skin.class);
            else if (asset.contains("textures/")) {
                if (asset.matches(".+\\.atlas")) game.assets.load(asset, TextureAtlas.class);
                else game.assets.load(asset, Texture.class);
            }
            else if (asset.contains("sounds/")) game.assets.load(asset, Sound.class);
            else if (asset.contains("music/")) game.assets.load(asset, Music.class);
            else if (asset.contains("tiledMaps")) {
                game.assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
                game.assets.load(asset, TiledMap.class);
            }
        }
        /* Template for loading fonts using asset manager
        FileHandleResolver resolver = new InternalFileHandleResolver();
        game.assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        game.assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter font = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        font.fontFileName = "fonts/Play-Bold.ttf";
        font.fontParameters.characters = Values.DEFAULT_CHARS;
        font.fontParameters.size = 64;
        font.fontParameters.spaceX = -3;
        font.fontParameters.borderWidth = 3;
        font.fontParameters.borderColor = new com.badlogic.gdx.graphics.Color(0f, 0f, 0f, 0f);
        font.fontParameters.color = new com.badlogic.gdx.graphics.Color(0f, 0f, 0f, 1f);
        game.assets.load("fonts/Play-Bold.ttf", BitmapFont.class, font);*/
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.312f, 0.312f, 0.312f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        boolean isFinished = game.assets.update();
        progress = game.assets.getProgress();
        // Decorative shadow
        stage.getBatch().begin();
        stage.getBatch().setColor(new Color(0f, 0f, 0f, progress));
        stage.getBatch().draw(shadow, 0, 0, Gdx.graphics.getWidth(), shadowHeight);
        stage.getBatch().draw(shadow, 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -shadowHeight);
        stage.getBatch().setColor(Color.WHITE);
        stage.getBatch().end();
        // Setting up progress bar and stage's root table
        if (game.assets.isLoaded(Values.SKIN_PATH, Skin.class)) {
            if (setUpProgressBar) {
                skin = game.assets.get(Values.SKIN_PATH, Skin.class);
                Drawable progressBarBackground = skin.getDrawable("progress-bar");
                progressBarBackground.setMinHeight(30f);
                Drawable progressBarKnob = skin.getDrawable("progress-bar-knob");
                progressBarKnob.setMinHeight(24f);
                progressBarStyle = new ProgressBar.ProgressBarStyle(progressBarBackground, progressBarKnob);
                progressBarStyle.knobBefore = skin.getDrawable("progress-bar-knob");
                progressBar = new ProgressBar(0f, 100f, 1f, false, progressBarStyle);
                percentageLabel = new Label("0%", skin);
                percentageLabel.setFontScale(Values.loadingScreenUIScale);
                loadingTimeLabel = new Label("0s", skin);
                loadingTimeLabel.setFontScale(Values.loadingScreenUIScale);
                versionLabel = new Label("v" + Values.VERSION, skin);
                versionLabel.setFontScale(Values.loadingScreenUIScale);
                rootTable.add(topTable).uniform().row();
                rootTable.add(centerTable).expand().row();
                rootTable.add(bottomTable).uniform().expandX().fill();
                rootTable.bottom();
                bottomTable.add(loadingTimeLabel).left().padLeft(5f).padBottom(5f).uniform();
                bottomTable.add(percentageLabel).padBottom(5f).expandX();
                bottomTable.add(versionLabel).right().padRight(5f).padBottom(5f).uniform().row();
                bottomTable.add(progressBar).expandX().fill().colspan(3);
                setUpProgressBar = false;
            }
            percentageLabel.setText(round(game.assets.getProgress() * 100.0f) + "%");
            loadingTimeLabel.setText(round(loadingTime * 10.0f) / 10.0f + "s");
            progressBar.setValue(progress * 100f);
        }
        // Setting up logo
        if (game.assets.isLoaded("textures/loadingScreenLogo.png", Texture.class)) {
            if (setUpLogo) {
                logoImage = new Image(game.assets.get("textures/loadingScreenLogo.png", Texture.class));
                centerTable.add(logoImage);
                setUpLogo = false;
            }
            logoImage.setColor(1f, 1f, 1f, progress);
        }
        stage.draw();
        stage.act();
        // Jump to another screen
        if (isFinished) {
            game.assets.finishLoading();
            if (!Values.stayOnLoadingScreen) {
                dispose();
                if (!Values.skipMainMenuScreen) game.setScreen(new MainMenuScreen(game));
                else game.setScreen(new GameScreen(game));
            }
        } else loadingTime += delta;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        game.assets.unload("textures/loadingScreenLogo.png");
    }
}
