package com.imaginegames.game.screens.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.imaginegames.game.MyGameMain;
import com.imaginegames.game.Values;
import com.imaginegames.game.ui.chat.ChatLabel;
import com.imaginegames.game.ui.chat.ChatLogger;

abstract class UI implements com.imaginegames.game.screens.UI {
    final MyGameMain game;
    final ClickListener textFieldClickListener = new ClickListener();
    final String senderName = "leonidsah";
    boolean showUI = true, showChat = true;
    Skin skin;
    Stage stage;
    ScreenViewport uiViewport;
    Table rootGameTable, topTable, bottomTable, rootMenuTable;
    Drawable menuButtonDown, menuButtonUp, chatButtonDown, chatButtonUp, inventoryButtonUp, inventoryButtonDown;
    ImageButton menuButton, chatButton, inventoryButton; // FIXME: 16.08.2021 Rework buttons using ninepatches
    TextField textField;
    ChatLabel chat;
    BitmapFont chatBitmapFont;
    Label.LabelStyle chatLabelStyle;
    FPSLogger fpsLogger;
    Touchpad touchpad;
    TextureAtlas textureAtlas;
    Sprite background;
    VerticalGroup verticalGroup;
    TextButton backToMenuButton, resumeButton;
    Stack rootStack;


    UI (MyGameMain game) { this.game = game; }

    private void initUI() {
        // Initialize assets
        skin = game.assets.get(Values.SKIN_PATH, Skin.class);
        textureAtlas = game.assets.get("textures/pack.atlas", TextureAtlas.class);
        background = textureAtlas.createSprite("background1");
        chatButtonDown = new TextureRegionDrawable(textureAtlas.createSprite("chatButtonDown"));
        chatButtonUp = new TextureRegionDrawable(textureAtlas.createSprite("chatButtonUp"));
        inventoryButtonDown = new TextureRegionDrawable(textureAtlas.createSprite("inventoryButtonDown"));
        inventoryButtonUp = new TextureRegionDrawable(textureAtlas.createSprite("inventoryButtonUp"));
        menuButtonDown = new TextureRegionDrawable(textureAtlas.findRegion("menuButtonDown"));
        menuButtonUp = new TextureRegionDrawable(textureAtlas.findRegion("menuButtonUp"));
        TextureRegion region = skin.getAtlas().findRegion("press-start-2p");
        chatBitmapFont = new BitmapFont(Gdx.files.internal("skins/commodore-65/press-start-2p.fnt"),
                skin.getAtlas().findRegion("press-start-2p"), false);
        // Initialize stage & actors
        // Set up
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.setDebugAll(Values.debugMode);
        rootStack = new Stack();
        rootGameTable = new Table();
        rootMenuTable = new Table();
        chatBitmapFont.getData().markupEnabled = true;
        chatLabelStyle = new Label.LabelStyle(chatBitmapFont, Color.WHITE);
        chatButton = new ImageButton(chatButtonUp, chatButtonDown);
        inventoryButton = new ImageButton(inventoryButtonUp, inventoryButtonDown);
        menuButton = new ImageButton(menuButtonUp, menuButtonDown);
        resumeButton = new TextButton("resume", skin);
        backToMenuButton = new TextButton("save & back to menu ", skin);
        chat = new ChatLabel(chatLabelStyle, 10);
        textField = new TextField("", skin, "default");
        touchpad = new Touchpad(0f, skin);
        topTable = new Table();
        bottomTable = new Table();
        uiViewport = new ScreenViewport();
        verticalGroup = new VerticalGroup();


        rootGameTable.setFillParent(true);
        stage.addActor(rootStack);
        rootStack.setFillParent(true);
        stage.setViewport(uiViewport);
        verticalGroup.addActor(resumeButton);
        verticalGroup.addActor(backToMenuButton);
        rootMenuTable.setVisible(false);
        verticalGroup.fill();
        verticalGroup.space(10);
        rootStack.add(rootGameTable);
        rootStack.add(rootMenuTable);
        rootMenuTable.add(verticalGroup);
        chat.setWrap(true);
        textField.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        textField.setMaxLength(64);
        // Adding listeners to actors
        externalShow();
        // Setting up tables
        topTable.add(textField).expandX().fill();
        topTable.add(chatButton);
        topTable.add(inventoryButton);
        topTable.add(menuButton);
        bottomTable.add(touchpad);
        rootGameTable.add(topTable).expandX().fill().right();
        rootGameTable.top().row();
        rootGameTable.add(chat).left().padLeft(10f).fill();
        rootGameTable.row();
        rootGameTable.add(bottomTable).expand().bottom().left();

    }

    @Override
    public final void show() {
        initUI();
        fpsLogger = new FPSLogger();
        if (Gdx.app.getType() == Application.ApplicationType.Android || Values.logUsingChat) Gdx.app.setApplicationLogger(new ChatLogger(chat));
    }

    @Override
    public final void draw() {
        stage.draw();
    }

    @Override
    public final void act(float delta) {
        stage.act(delta);
        if (Values.logFPS) fpsLogger.log();

    }

    @Override
    public final void dispose() { stage.dispose(); }

    @Override
    public final void resize(int width, int height) { stage.getViewport().update(width, height, true); }

    final void toggleAllVisibility() {
        showUI = !showUI;
        showChat = showUI;
        rootGameTable.setVisible(showUI);
    }

    final void toggleChatVisibility() {
        showChat = !showChat;
        textField.setVisible(showChat);
        chat.setVisible(showChat);
    }

    final void unfocusChat() {
        stage.setKeyboardFocus(null);
        Gdx.input.setOnscreenKeyboardVisible(false);
    }

    abstract void externalShow();
}
