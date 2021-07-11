package com.imaginegames.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.imaginegames.game.MilitaryMadnessMain;
import com.imaginegames.game.screens.GameScreen;
import com.imaginegames.game.screens.MainMenuScreen;
import com.imaginegames.game.utils.GameProcessor;
import com.imaginegames.game.utils.Values;

public class UIProcessor {
    // UI part TODO: Add FPS tracker via scene2d special tool
    // TODO: Chat - it's a ArrayList
    private MilitaryMadnessMain game;
    private GameProcessor gameProcessor;
    private GameScreen gameScreen;
    private Stage stage;
    private OrthographicCamera cam;
    private FillViewport fillViewport;
    private BitmapFont fontInfo;
    private Skin skin;
    private GlyphLayout camXPositionLabel, camYPositionLabel, scaleTrackerLabel;
    private Texture  menuButtonDown, menuButtonUp, chatButtonDown, chatButtonUp, inventoryButtonUp, inventoryButtonDown;
    private Table rootTable, topTable;
    private TextureButton menuButton, chatButton, inventoryButton;
    private TextField textField;
    private ChatLabel chatLabel;
    private ScrollPane scrollPane;
    private boolean showTextField = true;
    private float cameraAngle = 0;
    private TextMessage textMessage = new TextMessage("lol");

    public UIProcessor(GameProcessor gameProcessor) {
        this.gameProcessor = gameProcessor;
        this.gameScreen = gameProcessor.getGameScreen();
        this.game = gameProcessor.getGame();
        this.cam = gameProcessor.getCamera();
        this.fillViewport = gameProcessor.getViewport();
    }
    public void initAssets() {
        skin = game.assets.get(Values.SKIN_PATH, Skin.class);
        chatButtonDown = game.assets.get("textures/chatButtonDown.png", Texture.class);
        chatButtonUp = game.assets.get("textures/chatButtonUp.png", Texture.class);
        inventoryButtonDown = game.assets.get("textures/inventoryButtonDown.png", Texture.class);
        inventoryButtonUp = game.assets.get("textures/inventoryButtonUp.png", Texture.class);
        menuButtonDown = game.assets.get("textures/menuButtonDown.png", Texture.class);
        menuButtonUp = game.assets.get("textures/menuButtonUp.png", Texture.class);

    }
    public void init() {
        initAssets();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.setDebugAll(Values.STAGES_DEBUG);
        rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        chatLabel = new ChatLabel(skin, "default");
        chatButton = new TextureButton(chatButtonUp, chatButtonDown);
        inventoryButton = new TextureButton(inventoryButtonUp, inventoryButtonDown);
        menuButton = new TextureButton(menuButtonUp, menuButtonDown);
        textField = new TextField("", skin, "default");
        textField.setColor(0.5f, 0.5f, 0.5f, 0.5f);

        topTable = new Table();
        topTable.add(textField).expandX().fill();
        topTable.add(chatButton);
        topTable.add(inventoryButton);
        topTable.add(menuButton);
        rootTable.add(topTable).expandX().fill().right();
        rootTable.top().row();
        rootTable.add(chatLabel).left().padLeft(10f);
        chatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //showTextField = !showTextField;
                //textField.setVisible(showTextField);
                chatLabel.handle(new TextMessage(textField.getText()));

            }
        });
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new MainMenuScreen(game));
                gameProcessor.dispose();
            }
        });
    }
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }
    public void dispose() {
        stage.dispose();
    }
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
