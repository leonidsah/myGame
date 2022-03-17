package com.imaginegames.game.screens.worldmanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.imaginegames.game.MilitaryMadnessMain;
import com.imaginegames.game.Values;
import com.imaginegames.game.ui.chat.ChatLabel;

abstract class UI implements com.imaginegames.game.screens.UI {
    final MilitaryMadnessMain game;
    Skin skin;
    Stage stage;
    ScreenViewport screenViewport;
    Table rootTable, bottomTable, centerTable, topTable, textFieldsTable;
    ProgressBar progressBar;
    ProgressBar.ProgressBarStyle progressBarStyle;
    TextButton toggleCreateSectionButton, toggleLoadSectionButton, returnToMainMenuButton,
            proceedLoadButton, proceedCreateButton;
    TextField worldNameTextField, seedTextField;
    ChatLabel logChat;
    boolean loadSection = false, createSection = false;
    List<FileHandle> worldsList;
    ScrollPane worldScrollableList;
    Stack stack;

    UI (MilitaryMadnessMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        skin = game.assets.get(Values.SKIN_PATH, Skin.class);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.setDebugAll(Values.debugMode);
        rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);
        screenViewport = new ScreenViewport();
        stage.setViewport(screenViewport);

        topTable = new Table();
        centerTable = new Table();
        bottomTable = new Table();
        toggleCreateSectionButton = new TextButton("Create new world", skin);
        toggleLoadSectionButton = new TextButton("Load world", skin);
        returnToMainMenuButton = new TextButton("Return", skin);
        worldNameTextField = new TextField("", skin, "default");
        worldNameTextField.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        worldNameTextField.setMessageText("Name of the world");
        seedTextField = new TextField("", skin);
        seedTextField.setMessageText("Seed (optional)");
        seedTextField.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        logChat = new ChatLabel(skin, 7);
        logChat.setWrap(true);
        logChat.clearChat();
        worldsList = new List<FileHandle>(skin);
        worldScrollableList = new ScrollPane(worldsList, skin);
        proceedLoadButton = new TextButton("Proceed load", skin);
        proceedCreateButton = new TextButton("Proceed create", skin);
        textFieldsTable = new Table();
        textFieldsTable.add(worldNameTextField).expandX().fill().padBottom(10f).row();
        textFieldsTable.add(seedTextField).expandX().fill();
        stack = new Stack(textFieldsTable, worldScrollableList);

        rootTable.add(topTable).expand().fill().uniform().row();
        rootTable.add(centerTable).expand().fill().uniform().row();
        rootTable.add(bottomTable).expandX();
        bottomTable.add(toggleCreateSectionButton).uniform().fill();
        bottomTable.add(toggleLoadSectionButton).uniform().fill();
        bottomTable.add(returnToMainMenuButton).uniform().fill().row();
        bottomTable.add(proceedCreateButton).uniform().fill();
        bottomTable.add(proceedLoadButton).uniform().fill();
        topTable.add(logChat).expand().fill();
        centerTable.add(stack).expand().fill();

        worldScrollableList.setVisible(false);
        logChat.setVisible(false);
        textFieldsTable.setVisible(false);
        proceedCreateButton.setVisible(false);
        proceedLoadButton.setVisible(false);

        externalShow();

        toggleCreateSectionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                createSection = !createSection;
                returnToMainMenuButton.setVisible(!createSection);
                toggleLoadSectionButton.setVisible(!createSection);
                textFieldsTable.setVisible(createSection);
                logChat.setVisible(createSection);
                if (createSection) {
                    toggleCreateSectionButton.setText("Go back");
                    logChat.handleConsoleMessage("Type name of the world and seed to proceed");
                }
                else {
                    toggleCreateSectionButton.setText("Create new world");
                    proceedCreateButton.setVisible(false);
                    logChat.clearChat();
                }
            }
        });
        toggleLoadSectionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                loadSection = !loadSection;
                returnToMainMenuButton.setVisible(!loadSection);
                toggleCreateSectionButton.setVisible(!loadSection);
                worldScrollableList.setVisible(loadSection);
                proceedLoadButton.setVisible(true);
                logChat.setVisible(loadSection);
                if (loadSection) {
                    toggleLoadSectionButton.setText("Go back");
                    logChat.handleConsoleMessage("Choose world to proceed");
                }
                else {
                    toggleLoadSectionButton.setText("Load world");
                    proceedLoadButton.setVisible(false);
                    logChat.clearChat();
                }

            }
        });
        worldNameTextField.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                proceedCreateButton.setVisible(!worldNameTextField.getText().equals(""));
                return super.keyTyped(event, character);
            }
        });
    }

    @Override
    public void draw() {
        stage.draw();
    }

    @Override
    public void act(float delta) {
        stage.act();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    abstract void externalShow();
}
