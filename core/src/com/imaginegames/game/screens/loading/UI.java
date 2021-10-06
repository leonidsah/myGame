package com.imaginegames.game.screens.loading;

import static java.lang.Math.round;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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

class UI implements com.imaginegames.game.screens.UI {
    final MilitaryMadnessMain game;
    Skin skin;
    Stage stage;
    ScreenViewport screenViewport;
    Table rootTable, bottomTable, centerTable, topTable;
    ProgressBar progressBar;
    ProgressBar.ProgressBarStyle progressBarStyle;
    Label percentageLabel, loadingTimeLabel, versionLabel;
    Image logoImage;
    Pixmap shadowPixmap;
    Texture shadow;
    int shadowHeight = 256;
    float progress = 0f;
    float loadingTime;
    boolean setUpProgressBar = true, setUpLogo = true;
    UI (MilitaryMadnessMain game) { this.game = game; }

    @Override
    public final void show() {
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
    }

    @Override
    public final void draw() {
        stage.draw();
    }

    @Override
    public void act(float delta) {
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
        stage.act();
    }

    @Override
    public final void dispose() {
        stage.dispose();
    }

    @Override
    public final void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    void external() {}
}

