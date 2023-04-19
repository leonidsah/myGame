package com.imaginegames.game.screens.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.imaginegames.game.MyGameMain;
import com.imaginegames.game.screens.mainmenu.MainMenuScreen;
import com.imaginegames.game.Values;
import com.imaginegames.game.ui.chat.Command;
import com.imaginegames.game.ui.chat.messages.TextMessage;
import com.imaginegames.game.worlds.Chunk;
import com.imaginegames.game.worlds.ChunkRenderer;
import com.imaginegames.game.worlds.ChunkManager;
import com.imaginegames.game.worlds.ProceduralWorld;

import java.util.ArrayList;

public class GameScreen implements com.badlogic.gdx.Screen {
    private final MyGameMain game;
    private final SpriteBatch sb = new SpriteBatch();
    private UI ui;
    private OrthographicCamera cam;
    private ScreenViewport gameViewport;
    private float aspectRatio;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer tiledMapTileLayer;
    private float unitScale = 1 / 32f;
    private ArrayList<Sprite> rectangleSprites;
    private BitmapFont font;

    private ProceduralWorld world;
    private ChunkManager chunkManager;
    private ChunkRenderer chunkRenderer;
    private Chunk[][] chunkField;
    byte chunksFieldWidth, chunksFieldHeight, chunkSize;
    private float cursorX, cursorY;
    private Texture cursorTexture;


    float time = 0, lastTime = 0;

    public GameScreen(MyGameMain game, ProceduralWorld world) {
        this.game = game;
        this.world = world;
    }

    private void initAssets() {
        tiledMap = game.assets.get("tiledMaps/tiledMap.tmx");
        // Setting up a separate bitmap font with {color markup = enabled} for labels
        font = game.assets.get("fonts/luxi.fnt", BitmapFont.class);
        font.getData().setScale(1 / 32f);
    }

    @Override
    public void show() {
        ui = new UI(game) {
            @Override
            void externalShow() {
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
                        rootMenuTable.setVisible(true);
                    }
                });
                inventoryButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        toggleChatVisibility();
                        super.clicked(event, x, y);
                    }
                });
                backToMenuButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        chunkManager.saveChunks();
                        game.setScreen(new MainMenuScreen(game));
                    }
                });
                resumeButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        rootMenuTable.setVisible(false);
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
                chat.getCommandProcessor().addCommand(new Command("cam.translate", true) {
                    @Override
                    public void execute(String[] argv) {
                        if (argv.length == 2) {
                            if (argv[0].matches("(-)?[0-9]{1,4}") && argv[1].matches("(-)?[0-9]{1,4}")) {
                                cam.translate(Integer.parseInt(argv[0]), Integer.parseInt(argv[1]));
                                chat.handleConsoleMessage("Current camera's position: " + cam.position.x + " " + cam.position.y);
                            } else {
                                chat.handleConsoleMessage("[RED]Wrong arguments. Values are too big or not integer[]");
                            }
                        } else
                            chat.handleConsoleMessage("[RED]Wrong arguments. Usage: /cam.translate (-x) (-y)[]");
                    }
                });
                chat.getCommandProcessor().addCommand(new Command("cam.zoom", false) {
                    @Override
                    public void execute(String[] argv) {
                        if (argv.length == 1) {
                            if (argv[0].matches("(-)?[0-9]")) {
                                cam.zoom += Integer.parseInt(argv[0]);
                                chat.handleConsoleMessage("Current camera's zoom: " + cam.zoom);
                            } else { chat.handleConsoleMessage("[RED]Wrong arguments. Values are too big or not integer[]"); }
                        } else chat.handleConsoleMessage("[RED]Wrong arguments. Usage: /cam.zoom (-zoomBy)[]");
                    }
                });
                chat.getCommandProcessor().addCommand(new Command("javaHeap", false) {
                    @Override
                    public void execute() {
                        chat.handleConsoleMessage("[#4a6eb6]jheap: " + (Gdx.app.getJavaHeap() / (1024 * 1024)) + "[]");
                    }
                });
            }
        };
        ui.show();
        ui.chat.handleConsoleMessage("Use WASD and Shift to move");

        initAssets();
        handleControlKeys();
        // Setting up the camera
        aspectRatio = 9f / 16f;//(float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        cam = new OrthographicCamera();
        cam.zoom = 3.0f;
        gameViewport = new ScreenViewport(cam);
        gameViewport.setUnitsPerPixel(unitScale);

        // Tiled map part
        //mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);

        // Worlds
        //WorldManager.save(WorldManager.WorldGenerator.logarithmic(90, 90), "world.mm");
        Pixmap pix = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pix.setColor(Color.RED);
        pix.fillRectangle(12, 0, 8, 32);
        pix.fillRectangle(0, 12, 32, 8);
        pix.setColor(Color.DARK_GRAY);
        pix.fillRectangle(13, 1, 6, 30);
        pix.fillRectangle(1, 13, 30, 6);
        cursorTexture = new Texture(pix);
        pix.dispose();

        chunkManager = new ChunkManager(world, (byte) 16, (byte) 10);

        chunksFieldWidth = chunkManager.getChunkFieldWidth();
        chunksFieldHeight = chunkManager.getChunkFieldHeight();
        chunkSize = chunkManager.getChunkSize();

        chunkRenderer = new ChunkRenderer(sb, chunksFieldWidth, chunksFieldHeight, chunkSize);

        chunkManager.updateChunkField(0, 0);
        chunkField = chunkManager.getChunkField();


        cursorX = 0;
        cursorY = 0;
        //cursorX += chunksFieldWidth % 2 != 0 ? chunkSize / 2.0f : 0;
        //cursorY += chunksFieldHeight % 2 != 0 ? chunkSize / 2.0f : 0;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ui.act(delta);
        handlePlayerControl(delta);

        time += delta;
        if (time - lastTime >= 0.8f) {
            lastTime = time;
            ui.chat.handleConsoleMessage("cellX: " + Chunk.getCellCoord(cursorX, chunkSize) + "    x: " + cursorX);
        }

        cam.translate(cursorX - cam.position.x, cursorY - cam.position.y);
        cam.update();
        sb.setProjectionMatrix(cam.combined);

        chunkManager.updateChunkField(Chunk.getChunkCoord(cursorX, chunkSize), Chunk.getChunkCoord(cursorY, chunkSize));
        chunkField = chunkManager.getChunkField();
        chunkRenderer.render(chunkField, cursorX, cursorY);

        sb.begin();
        float cursorWidth = 1, cursorHeight = 1;
        sb.draw(cursorTexture, cursorX - cursorWidth / 2.0f, cursorY - cursorHeight / 2.0f, cursorWidth, cursorHeight);
        sb.end();
        ui.draw();
    }

    @Override
    public void resize(int width, int height) {
        ui.resize(width, height);
        gameViewport.update(width, height, false);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        ui.dispose();
        Gdx.input.setInputProcessor(null);
        sb.dispose();
        cursorTexture.dispose();
    }

    public void handleControlKeys() {
        ui.stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                // Platform-independent control keys
                if (keycode == Input.Keys.ENTER) {
                    ui.chat.handle(new TextMessage(ui.textField.getText(), ui.senderName));
                    ui.textField.setText("");
                    Gdx.input.setOnscreenKeyboardVisible(false);
                }
                // Desktop control keys
                if (Gdx.app.getType() != Application.ApplicationType.Desktop) return super.keyDown(event, keycode);
                if (keycode == Input.Keys.ESCAPE) {
                    if (ui.stage.getKeyboardFocus() != null) ui.stage.setKeyboardFocus(null);
                    else {
                        ui.rootMenuTable.setVisible(!ui.rootMenuTable.isVisible());
                    }
                }
                if (ui.stage.getKeyboardFocus() == null) {
                    if (keycode == Input.Keys.UP) cam.translate(0f, 1f * cam.zoom);
                    if (keycode == Input.Keys.DOWN) cam.translate(0f, -1f * cam.zoom);
                    if (keycode == Input.Keys.LEFT) cam.translate(-1f * cam.zoom, 0f);
                    if (keycode == Input.Keys.RIGHT) cam.translate(1f * cam.zoom, 0f);
                    if (keycode == Input.Keys.UP || keycode == Input.Keys.DOWN || keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT) {
                        ui.chat.handleConsoleMessage("Current camera's position: " + cam.position.x + " " + cam.position.y);
                    }
                    if ((keycode == Input.Keys.EQUALS || keycode == Input.Keys.RIGHT_BRACKET) && cam.zoom > 0.1f) {
                        cam.zoom -= 0.1f;
                        ui.chat.handleConsoleMessage("Current camera's zoom: " + cam.zoom);
                    }
                    if ((keycode == Input.Keys.MINUS || keycode == Input.Keys.LEFT_BRACKET) && cam.zoom < 8.0f) {
                        cam.zoom += 0.1f;
                        ui.chat.handleConsoleMessage("Current camera's zoom: " + cam.zoom);
                    }
                    if (keycode == Input.Keys.C) ui.toggleChatVisibility();
                    if (keycode == Input.Keys.U) ui.toggleAllVisibility();
                    if (keycode == Input.Keys.F) {
                        Values.fullscreenMode = !Values.fullscreenMode;
                        if (Values.fullscreenMode)
                            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                        else Gdx.graphics.setWindowedMode(1280, 720);
                    }
                    if (keycode == Input.Keys.F1) {
                        Values.logFPS = !Values.logFPS;
                        Gdx.app.log("Console", "FPS logging: " + (Values.logFPS ? "enabled" : "disabled"));
                    }
                    if (keycode == Input.Keys.SPACE) {
                        chunkManager.setCell(cursorX, cursorY, 250);
                    }
                    if (keycode == Input.Keys.M) {

                    }
                } else {
                    if (keycode == Input.Keys.UP) {
                        //textField.setText(chatLabel.getPreviousMessage() != null ? chatLabel.getPreviousMessage().getContext() : ""); // TODO: <<<
                    }
                }
                return super.keyDown(event, keycode);
            }

        });
    }

    public void handlePlayerControl(float delta) {
        float speed = 25;
        float shiftSpeed;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            shiftSpeed = speed * 2;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            shiftSpeed = speed / 4;
        }
        else shiftSpeed = speed;
        if (ui.stage.getKeyboardFocus() == null) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) cursorY += shiftSpeed * delta;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) cursorX -= shiftSpeed * delta;
            if (Gdx.input.isKeyPressed(Input.Keys.S)) cursorY -= shiftSpeed * delta;
            if (Gdx.input.isKeyPressed(Input.Keys.D)) cursorX += shiftSpeed * delta;
        }
        float tpadX = ui.touchpad.getKnobPercentX();
        float tpadY = ui.touchpad.getKnobPercentY();
        float r = tpadX * tpadX + tpadY * tpadY;

        if (r < 0.9f * 0.9f) {
            cursorX += speed * delta * tpadX;
            cursorY += speed * delta * tpadY;
        }
        else if (r > 0.9f * 0.9f) {
            cursorX += (speed * 2) * delta * tpadX;
            cursorY += (speed * 2) * delta * tpadY;
        }
        // TODO: Debug cursor speed and position. Make a special class/scene2d.ui widget for debug info
    }
}