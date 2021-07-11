package com.imaginegames.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.imaginegames.game.MilitaryMadnessMain;
import com.imaginegames.game.utils.Values;

public class LoadingScreen implements Screen {

    private MilitaryMadnessMain game;
    private float logoScale = 1.0f;
    private float logoWidth = 512 * logoScale;
    private float logoHeight = 256 * logoScale;
    private float progress = 0f;

    private float loadingTime;
    private Skin skin;
    private Stage stage;
    private Table rootTable, labelsTable;
    private ProgressBar progressBar;
    private ProgressBar.ProgressBarStyle progressBarStyle;
    private Label percentageLabel, loadingTimeLabel, verisonLabel;
    private boolean setUpActors = true;

    private String[] assetsArray;

    public LoadingScreen(MilitaryMadnessMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        /**The most necessary assets loading*/
        assetsArray = new String[]
                {
                        // Skins
                        Values.SKIN_PATH,

                        // Textures
                        "textures/loading_screen_logo.png",
                        "textures/shadow.png",
                        "textures/menuButtonUp.png",
                        "textures/menuButtonDown.png",
                        "textures/chatButtonUp.png",
                        "textures/chatButtonDown.png",
                        "textures/inventoryButtonUp.png",
                        "textures/inventoryButtonDown.png",
                        "textures/background1.png",

                        // Sounds
                        "sounds/go_sound.mp3",
                        "sounds/cancel_sound.mp3",

                        // Music
                        "music/Wind, winter and cold.mp3"
                };

        // Loading assets (including assets for loading screen)
        for (String asset: assetsArray) {
            if (asset.contains("skins/")) game.assets.load(asset, Skin.class);
            if (asset.contains("textures/")) game.assets.load(asset, Texture.class);
            if (asset.contains("sounds/")) game.assets.load(asset, Sound.class);
            if (asset.contains("music/")) game.assets.load(asset, Music.class);
        }
        // Setting up a stage
        stage = new Stage();
        rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);
        labelsTable = new Table();

        // Font for loading screen
        FreetypeFontLoader.FreeTypeFontLoaderParameter temp_font_param = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        temp_font_param.fontFileName = "fonts/Play-Bold_Loading_Screen.ttf";
        temp_font_param.fontParameters.characters = Values.DEFAULT_CHARS;
        temp_font_param.fontParameters.size = 40;
        temp_font_param.fontParameters.spaceX = -3;
        temp_font_param.fontParameters.borderWidth = 3;
        temp_font_param.fontParameters.borderColor = new com.badlogic.gdx.graphics.Color(0f, 0f, 0f, 0f);
        temp_font_param.fontParameters.color = new com.badlogic.gdx.graphics.Color(1f, 1f, 1f, 1f);
        game.assets.load("fonts/Play-Bold_Loading_Screen.ttf", BitmapFont.class, temp_font_param);

        FileHandleResolver resolver = new InternalFileHandleResolver();
        game.assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        game.assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        // Font for information (fully black)
        FreetypeFontLoader.FreeTypeFontLoaderParameter font_i = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        font_i.fontFileName = "fonts/Play-Regular_Info.ttf";
        font_i.fontParameters.characters = Values.DEFAULT_CHARS;
        font_i.fontParameters.size = 36;
        font_i.fontParameters.color = new com.badlogic.gdx.graphics.Color(0f, 0f, 0f, 1f);
        game.assets.load("fonts/Play-Regular_Info.ttf", BitmapFont.class, font_i);

        // Font
        FreetypeFontLoader.FreeTypeFontLoaderParameter font = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        font.fontFileName = "fonts/Play-Bold.ttf";
        font.fontParameters.characters = Values.DEFAULT_CHARS;
        font.fontParameters.size = 64;
        font.fontParameters.spaceX = -3;
        font.fontParameters.borderWidth = 3;
        font.fontParameters.borderColor = new com.badlogic.gdx.graphics.Color(0f, 0f, 0f, 0f);
        font.fontParameters.color = new com.badlogic.gdx.graphics.Color(0f, 0f, 0f, 1f);
        game.assets.load("fonts/Play-Bold.ttf", BitmapFont.class, font);

        // Font selected
        FreetypeFontLoader.FreeTypeFontLoaderParameter font_s = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        font_s.fontFileName = "fonts/Play-Bold_S.ttf";
        font_s.fontParameters.characters = Values.DEFAULT_CHARS;
        font_s.fontParameters.size = 64;
        font_s.fontParameters.spaceX = -3;
        font_s.fontParameters.borderWidth = 3;
        font_s.fontParameters.borderColor = new com.badlogic.gdx.graphics.Color(0f, 1f, 0f, 1f);
        font_s.fontParameters.color = new com.badlogic.gdx.graphics.Color(0f, 0f, 0f, 1f);
        game.assets.load("fonts/Play-Bold_S.ttf", BitmapFont.class, font_s);

        // Font for chosen
        FreetypeFontLoader.FreeTypeFontLoaderParameter font_c = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        font_c.fontFileName = "fonts/Play-Regular.ttf";
        font_c.fontParameters.characters = Values.DEFAULT_CHARS;
        font_c.fontParameters.size = 36;
        font_c.fontParameters.color = new com.badlogic.gdx.graphics.Color(0.5f, 0.5f, 0.5f, 1f);
        game.assets.load("fonts/Play-Regular.ttf", BitmapFont.class, font_c);

        // Font for events message (Font colored)
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontColored = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontColored.fontFileName = "fonts/Play-Regular.ttf";
        fontColored.fontParameters.characters = Values.DEFAULT_CHARS;
        fontColored.fontParameters.size = 36;
        fontColored.fontParameters.color = new com.badlogic.gdx.graphics.Color(0.565f, 0.933f, 0.565f, 1f);
        game.assets.load("fonts/Play-Regular_Colored.ttf", BitmapFont.class, fontColored);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.312f, 0.312f, 0.312f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        boolean isFinished = game.assets.update();
        progress = game.assets.getProgress();
        game.batch.begin();
        // Decorative shadow
        float shadow_scale = game.assets.getProgress();
        float shadowHeight = 256 * shadow_scale;
        if (game.assets.isLoaded("textures/shadow.png", Texture.class)) {
            game.batch.setColor(Color.BLACK);
            game.batch.draw(game.assets.get("textures/shadow.png", Texture.class), 0, 0, Gdx.graphics.getWidth(), shadowHeight);
            game.batch.draw(game.assets.get("textures/shadow.png", Texture.class), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -shadowHeight);
            game.batch.setColor(Color.WHITE);
        }
        // Progress bar
        if (game.assets.isLoaded(Values.SKIN_PATH, Skin.class)) {
            if (setUpActors) {
                skin = game.assets.get(Values.SKIN_PATH, Skin.class);
                Drawable progressBarBackground = skin.getDrawable("progress-bar");
                progressBarBackground.setMinHeight(30f);
                Drawable progressBarKnob = skin.getDrawable("progress-bar-knob");
                progressBarKnob.setMinHeight(24f);
                progressBarStyle = new ProgressBar.ProgressBarStyle(progressBarBackground, progressBarKnob);
                progressBarStyle.knobBefore = skin.getDrawable("progress-bar-knob");
                progressBar = new ProgressBar(0f, 100f, 1f, false, progressBarStyle);
                percentageLabel = new Label("0%", skin);
                percentageLabel.setFontScale(Values.LoadingScreenUIScale);
                loadingTimeLabel = new Label("0s", skin);
                loadingTimeLabel.setFontScale(Values.LoadingScreenUIScale);
                verisonLabel = new Label("v" + Values.VERSION, skin);
                verisonLabel.setFontScale(Values.LoadingScreenUIScale);
                rootTable.add(labelsTable).expandX().fill().row();
                rootTable.add(progressBar).fill();
                rootTable.bottom();
                labelsTable.add(loadingTimeLabel).padLeft(5f).padBottom(5f).uniform();
                labelsTable.add(percentageLabel).padBottom(5f).expandX();
                labelsTable.add(verisonLabel).padRight(5f).padBottom(5f).uniform();
                setUpActors = false;
            }
            percentageLabel.setText(Math.round(game.assets.getProgress() * 100.0f) + "%");
            loadingTimeLabel.setText(Math.round(loadingTime * 10.0f) / 10.0f + "s");
            progressBar.setValue(progress * 100f);
        }
        // Decorative logo
        if (game.assets.isLoaded("textures/loading_screen_logo.png", Texture.class)) {
            game.batch.draw(game.assets.get("textures/loading_screen_logo.png", Texture.class), (Gdx.graphics.getWidth() - logoWidth) / 2,
                    (Gdx.graphics.getHeight() - logoHeight) / 2, logoWidth, logoHeight);
            game.batch.setColor(1f, 1f, 1f, 1f);
        }
        game.batch.end();
        stage.draw();
        stage.act();
        // Jump to another screen
        if (isFinished) {
            game.assets.finishLoading();
            dispose();
            game.setScreen(new MainMenuScreen(game));
        }
        else loadingTime += delta;
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
        game.assets.unload("textures/shadow.png");
        game.assets.unload("textures/loading_screen_logo.png");
        game.assets.unload("fonts/Play-Bold_Loading_Screen.ttf");
    }
}
