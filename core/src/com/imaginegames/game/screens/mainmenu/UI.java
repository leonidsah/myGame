package com.imaginegames.game.screens.mainmenu;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.imaginegames.game.MilitaryMadnessMain;
import com.imaginegames.game.Values;

class UI implements com.imaginegames.game.screens.UI {
    final MilitaryMadnessMain game;
    Skin skin;
    Stage stage;
    ScreenViewport screenViewport;
    Table rootTable, leftTable, centerTable, rightTable;
    TextButton playButton, exitButton, settingsButton, showDebugDialogButton;
    Dialog debugDialog;
    boolean showSettings = false;
    CheckBox debugModeCheckBox, fullscreenModeCheckbox, logFPSCheckBox;
    float textButtonScale = Values.mainMenuScreenUIScale;
    float checkBoxScale = Values.mainMenuScreenUIScale / 2.0f;
    UI (MilitaryMadnessMain game) { this.game = game; }

    @Override
    public final void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.setDebugAll(Values.stagesDebug);
        rootTable = new Table();
        stage.addActor(rootTable);
        rootTable.setFillParent(true);
        screenViewport = new ScreenViewport();
        stage.setViewport(screenViewport);
        skin = game.assets.get(Values.SKIN_PATH, Skin.class);
        // Initializing and setting up widgets
        centerTable = new Table();
        leftTable = new Table();
        rightTable = new Table();
        playButton = new TextButton("Play", skin);
        settingsButton = new TextButton("Settings", skin);
        exitButton = new TextButton("Exit", skin);
        debugModeCheckBox = new CheckBox("Debug mode", skin);
        debugModeCheckBox.setChecked(Values.stagesDebug);
        fullscreenModeCheckbox = new CheckBox("Fullscreen", skin);
        logFPSCheckBox = new CheckBox("Log FPS", skin);
        logFPSCheckBox.setChecked(Values.logFPS);
        showDebugDialogButton = new TextButton("Debug info", skin);
        debugDialog = new Dialog("Debug info dialog", skin) {
            @Override
            protected void result(Object object) {
                super.result(object);
            }
        };
        debugDialog.button("Got it");
        debugDialog.setColor(Color.GRAY);
        // Adding listeners to scene2d.ui actors
        external();
    }

    @Override
    public final void draw() {
        stage.draw();
    }

    @Override
    public final void act(float delta) {
        stage.act();
    }

    @Override
    public final void dispose() {
        stage.dispose();
    }

    @Override
    public final void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        if (width < 850 || height < 440) Values.mainMenuScreenUIScale = 1f;
        else if ((width < 1280 && width >= 850) || (height < 720 && width >= 440)) Values.mainMenuScreenUIScale = 2f;
        else Values.mainMenuScreenUIScale = 3f;
        textButtonScale = Values.mainMenuScreenUIScale;
        checkBoxScale = Values.mainMenuScreenUIScale / 2.0f;
        playButton.getLabel().setFontScale(textButtonScale);
        settingsButton.getLabel().setFontScale(textButtonScale);
        exitButton.getLabel().setFontScale(textButtonScale);
        debugModeCheckBox.getLabel().setFontScale(checkBoxScale);
        fullscreenModeCheckbox.getLabel().setFontScale(checkBoxScale);
        logFPSCheckBox.getLabel().setFontScale(checkBoxScale);
        rebuildTable();
    }

    final void rebuildTable() {
        leftTable.clear();
        centerTable.clear();
        rightTable.clear();
        rootTable.clear();
        // Adding widgets to tables
        centerTable.add(playButton).row();
        centerTable.add(settingsButton).space(40f).row();
        centerTable.add(exitButton).row();
        leftTable.center();
        leftTable.setVisible(showSettings);
        rightTable.setVisible(showSettings);
        // Setting up tables
        rootTable.add(leftTable).fill().prefWidth(400f).pad(10f).uniform();
        rootTable.add(centerTable).expand();
        rootTable.add(rightTable).fill().pad(10f).uniform();
        leftTable.add(debugModeCheckBox).row();
        leftTable.add(logFPSCheckBox);
        rightTable.add(showDebugDialogButton).right().prefHeight(64f);
        // Desktop UI part
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            fullscreenModeCheckbox.setChecked(Values.fullscreenMode);
            leftTable.row();
            leftTable.add(fullscreenModeCheckbox);
        }
    }

    void external() {}
}
