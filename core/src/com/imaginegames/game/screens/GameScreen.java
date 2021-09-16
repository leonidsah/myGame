package com.imaginegames.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.imaginegames.game.MilitaryMadnessMain;
import com.imaginegames.game.ui.chat.ChatLabel;
import com.imaginegames.game.ui.chat.ChatLogger;
import com.imaginegames.game.ui.chat.Command;
import com.imaginegames.game.ui.chat.TextMessage;
import com.imaginegames.game.Values;
import com.imaginegames.game.utils.MapValue;
import com.imaginegames.game.utils.PerlinNoise;
import com.imaginegames.game.worlds.World;
import com.imaginegames.game.worlds.WorldGenerator;
import com.imaginegames.game.worlds.WorldManager;

import java.util.ArrayList;

public class GameScreen implements Screen {
    private final MilitaryMadnessMain game;
    private final String senderName = "leonidsah";
    private final ClickListener textFieldClickListener = new ClickListener();
    private OrthographicCamera cam;
    private ScreenViewport gameViewport;
    private float aspectRatio;
    private TextureAtlas textureAtlas;
    private Sprite background;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Stage stage;
    private ScreenViewport uiViewport;
    private BitmapFont fontInfo;
    private Skin skin;
    private GlyphLayout camXPositionLabel, camYPositionLabel, scaleTrackerLabel;
    private Drawable menuButtonDown, menuButtonUp, chatButtonDown, chatButtonUp, inventoryButtonUp, inventoryButtonDown;
    private Table rootTable, topTable;
    private ImageButton menuButton, chatButton, inventoryButton; // FIXME: 16.08.2021 Rework buttons using ninepatches
    private TextField textField;
    private ChatLabel chat;
    private BitmapFont chatBitmapFont;
    private Label.LabelStyle chatLabelStyle;
    private FPSLogger fpsLogger;
    private boolean showUI = true, showChat = true;
    private ScrollPane scrollPane;
    private Pixmap pixmap;
    private Texture playerTexture;
    private TiledMapTileLayer tiledMapTileLayer;
    private float unitScale = 1 / 32f;
    private ArrayList<Sprite> rectangleSprites;
    private WorldManager worldManager = new WorldManager();
    private World world;
    //private World world = WorldGenerator.generateRandom();
    int[][] blocks;
    float c = 0;


    public GameScreen(MilitaryMadnessMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        initAssets();
        // Setting up scene2d ui
        initUI();
        handleControlKeys();
        // Setting up the camera
        aspectRatio = 9f / 16f;//(float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        cam = new OrthographicCamera();
        gameViewport = new ScreenViewport(cam);
        gameViewport.setUnitsPerPixel(unitScale);
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        // Tiled map part
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);
        // Other
        if (Gdx.app.getType() == Application.ApplicationType.Android || Values.logUsingChat) Gdx.app.setApplicationLogger(new ChatLogger(chat));
        fpsLogger = new FPSLogger();
        // World (aka my own tile map) testing
        WorldManager worldManager = new WorldManager();
        loadWorld();
        // World init
        refreshWorldRendering();
    }

    public void refreshWorldRendering() {
        // World generating
        blocks = world.getBlocks();
        double t = 0;
        float alpha;
        pixmap = new Pixmap(world.getWorldWidth(), world.getWorldHeight(), Pixmap.Format.RGBA8888);

        for (int i = 0; i < world.getWorldHeight(); i++) {
            for (int j = 0; j < world.getWorldWidth(); j++) {
                alpha = (float) MapValue.map(PerlinNoise.noise((i + 0) / 10.0, (j + 0) / 10.0, c), -1.0, 1.0, 0.0, 1.0);
                //pixmap.setColor(0f, 1f, 0f, blocks[i][j] / 255f);
                pixmap.setColor(1f, 1f, 1f, alpha);
                //System.out.println(alpha);
                pixmap.drawPixel(j, i);
            }
        }
        playerTexture = new Texture(pixmap);
    }

    public void loadWorld() {
        Gdx.app.log("mm-world", "World loading started...");
        world = WorldManager.loadWorld("world.mm");
        Gdx.app.log("mm-world", "World loading done");
    }

    public void changeWorld() {
        Gdx.app.log("mm-world", "World generating started...");
        world = WorldGenerator.generateRandom();
        Gdx.app.log("mm-world", "World generating done");
        Gdx.app.log("mm-world", "World saving started...");
        worldManager.saveWorld(world, "world.mm");
        Gdx.app.log("mm-world", "World saving done");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Update camera
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        // Rendering map
        mapRenderer.setView(cam);
        //mapRenderer.render();
        // Drawing textures
        game.batch.begin();
        //game.batch.draw(background, (extendViewport.getWorldWidth() - 30) / 2.0f, (extendViewport.getWorldHeight() - 30 * aspectRatio) / 2.0f, 30, 30 * aspectRatio);
        game.batch.draw(playerTexture, 0, 0, playerTexture.getWidth() * unitScale, playerTexture.getHeight() * unitScale);
        game.batch.end();
        // UI
        stage.act(delta);
        stage.draw();
        if (Values.logFPS) fpsLogger.log();

        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            c = c + 0.01f;
            refreshWorldRendering();
        }
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height, true);
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
    }

    private void initAssets() {
        textureAtlas = game.assets.get("textures/pack.atlas", TextureAtlas.class);
        background = textureAtlas.createSprite("background1");
        tiledMap = game.assets.get("tiledMaps/tiledMap.tmx");
        skin = game.assets.get(Values.SKIN_PATH, Skin.class);
        chatButtonDown = new TextureRegionDrawable(textureAtlas.createSprite("chatButtonDown"));
        chatButtonUp = new TextureRegionDrawable(textureAtlas.createSprite("chatButtonUp"));
        inventoryButtonDown = new TextureRegionDrawable(textureAtlas.createSprite("inventoryButtonDown"));
        inventoryButtonUp = new TextureRegionDrawable(textureAtlas.createSprite("inventoryButtonUp"));
        menuButtonDown = new TextureRegionDrawable(textureAtlas.findRegion("menuButtonDown"));
        menuButtonUp = new TextureRegionDrawable(textureAtlas.findRegion("menuButtonUp"));
        // Setting up a separate bitmap font with {color markup = enabled} for labels
        TextureRegion region = skin.getAtlas().findRegion("press-start-2p");
        chatBitmapFont = new BitmapFont(Gdx.files.internal("skins/commodore-65/press-start-2p.fnt"), region, false);
        chatBitmapFont.getData().markupEnabled = true;
        chatLabelStyle = new Label.LabelStyle(chatBitmapFont, Color.WHITE);


    }

    public void initUI() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.setDebugAll(Values.stagesDebug);
        rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);
        uiViewport = new ScreenViewport();
        stage.setViewport(uiViewport);
        // Actors initialization
        chat = new ChatLabel(chatLabelStyle);
        chat.setWrap(true);
        chatButton = new ImageButton(chatButtonUp, chatButtonDown);
        inventoryButton = new ImageButton(inventoryButtonUp, inventoryButtonDown);
        menuButton = new ImageButton(menuButtonUp, menuButtonDown);
        textField = new TextField("", skin, "default");
        textField.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        textField.setMaxLength(64);
        // Setting up tables
        topTable = new Table();
        topTable.add(textField).expandX().fill();
        topTable.add(chatButton);
        topTable.add(inventoryButton);
        topTable.add(menuButton);
        rootTable.add(topTable).expandX().fill().right();
        rootTable.top().row();
        rootTable.add(chat).left().padLeft(10f).fill();
        chatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (showChat) chat.handle(new TextMessage(textField.getText(), senderName));
            }
        });
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });
        inventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hideChat();
                super.clicked(event, x, y);
            }
        });
        textField.addListener(textFieldClickListener);
        stage.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!textFieldClickListener.isOver()) unfocusChat();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private void hideChat() {
        showChat = !showChat;
        textField.setVisible(showChat);
        chat.setVisible(showChat);
    }

    private void hideUI() {
        showUI = !showUI;
        showChat = showUI;
        rootTable.setVisible(showUI);
    }

    private void unfocusChat() {
        stage.setKeyboardFocus(null);
        Gdx.input.setOnscreenKeyboardVisible(false);
    }

    public void handleControlKeys() {
        // Chat commands
        chat.getCommandProcessor().addCommand(new Command("cam.translate", true) {
            @Override
            public void execute(String[] argv) {
                for (String s : argv) System.out.println(s);
                if (argv.length == 2) {
                    if (argv[0].matches("(-)?[0-9]{1,4}") && argv[1].matches("(-)?[0-9]{1,4}")) {
                        cam.translate(Integer.parseInt(argv[0]), Integer.parseInt(argv[1]));
                        chat.handleConsoleMessage("Current camera's position: " + cam.position.x + " " + cam.position.y);
                    }
                    else {
                        chat.handleConsoleMessage("[RED]Wrong arguments. Values are too big or not integer[]");
                    }
                }
                else chat.handleConsoleMessage("[RED]Wrong arguments. Usage: /cam.translate (-x) (-y)[]");
            }
        });
        chat.getCommandProcessor().addCommand(new Command("cam.zoom", true) {
            @Override
            public void execute(String[] argv) {
                for (String s : argv) System.out.println(s);
                if (argv.length == 1) {
                    if (argv[0].matches("(-)?[0-9]")) {
                        cam.zoom += Integer.parseInt(argv[0]);
                        chat.handleConsoleMessage("Current camera's zoom: " + cam.zoom);
                    }
                    else {
                        chat.handleConsoleMessage("[RED]Wrong arguments. Values are too big or not integer[]");
                    }
                }
                else chat.handleConsoleMessage("[RED]Wrong arguments. Usage: /cam.zoom (-zoomBy)[]");
            }
        });
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                // Platform-independent control keys
                if (keycode == Input.Keys.ENTER) {
                    chat.handle(new TextMessage(textField.getText(), senderName));
                    textField.setText("");
                    Gdx.input.setOnscreenKeyboardVisible(false);
                }
                // Desktop control keys
                if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                    if (keycode == Input.Keys.ESCAPE) {
                        if (stage.getKeyboardFocus() != null) stage.setKeyboardFocus(null);
                        else {
                            game.setScreen(new MainMenuScreen(game));
                            dispose();
                        }
                    }
                    if (keycode == Input.Keys.UP) cam.translate(0f, 1f * cam.zoom);
                    if (keycode == Input.Keys.DOWN) cam.translate(0f, -1f * cam.zoom);
                    if (keycode == Input.Keys.LEFT) cam.translate(-1f * cam.zoom, 0f);
                    if (keycode == Input.Keys.RIGHT) cam.translate(1f * cam.zoom, 0f);
                    if ((keycode == Input.Keys.EQUALS || keycode == Input.Keys.RIGHT_BRACKET) && cam.zoom > 0.1f) cam.zoom -= 0.1f;
                    if ((keycode == Input.Keys.MINUS || keycode == Input.Keys.LEFT_BRACKET) && cam.zoom < 3.0f) cam.zoom += 0.1f;
                    if (stage.getKeyboardFocus() == null) {
                        if (keycode == Input.Keys.C) hideChat();
                        if (keycode == Input.Keys.U) hideUI();
                        if (keycode == Input.Keys.F) {
                            Values.fullscreenMode = !Values.fullscreenMode;
                            if (Values.fullscreenMode) Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                            else Gdx.graphics.setWindowedMode(1280, 720);
                        }
                        if (keycode == Input.Keys.F1) {
                            Values.logFPS = !Values.logFPS;
                            Gdx.app.log("Console", "FPS logging: " + (Values.logFPS ? "enabled" : "disabled"));
                        }
                        if (keycode == Input.Keys.F2);
                        if (keycode == Input.Keys.G) {

                        }
                    } else {
                        if (keycode == Input.Keys.UP) {
                            //textField.setText(chatLabel.getPreviousMessage() != null ? chatLabel.getPreviousMessage().getContext() : ""); // TODO: <<<
                        }
                    }
                }
                return super.keyDown(event, keycode);
            }

        });
    }
}