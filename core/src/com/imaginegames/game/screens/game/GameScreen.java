package com.imaginegames.game.screens.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.imaginegames.game.MyGame;
import com.imaginegames.game.screens.mainmenu.MainMenuScreen;
import com.imaginegames.game.Values;
import com.imaginegames.game.ui.chat.Command;
import com.imaginegames.game.ui.chat.messages.TextMessage;
import com.imaginegames.game.utils.math.IntPair;
import com.imaginegames.game.worlds.Chunk;
import com.imaginegames.game.worlds.ChunkManager;
import com.imaginegames.game.worlds.WorldManager;
import com.imaginegames.game.worlds.WorldRenderer;

import java.util.ArrayList;

public class GameScreen implements Screen {
    private final MyGame game;
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
    private Texture cursorTexture, chunksFieldTexture;
    private ChunkManager cm;
    private final int chunksFieldSize = 9;
    private Chunk[][] chunksField = new Chunk[chunksFieldSize][chunksFieldSize];
    private float cursorx = 0, cursory = 0, oldcursorx = 0, oldcursory = 0;
    private BitmapFont font;
    private Pixmap fieldpix;
    private int fieldTextureSize;
    private long time = 0;

    public GameScreen(MyGame game) {
        this.game = game;
    }

    private void initAssets() {
        tiledMap = game.assets.get("tiledMaps/tiledMap.tmx");
        // Setting up a separate bitmap font with {color markup = enabled} for labels
        font = game.assets.get("fonts/arial.fnt", BitmapFont.class);
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
                        game.setScreen(new MainMenuScreen(game));
                    }
                });
                inventoryButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        toggleChatVisibility();
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
                chat.getCommandProcessor().addCommand(new Command("world-reset", true) {
                    @Override
                    public void execute(String[] argv) {
                        chat.handleConsoleMessage("Resetting the world... ");
                        WorldManager.save(WorldManager.WorldGenerator.logarithmic(90, 90), "world.mm");
                    }
                });
                if (Gdx.app.getType() == Application.ApplicationType.Desktop)
                    chat.getCommandProcessor().addCommand(new Command("world-print", false) {
                        @Override
                        public void execute() {
                            chat.handleConsoleMessage("Printing the world... ");
                            WorldManager.synopsis(WorldManager.load("world.mm"), false);
                        }
                    });
            }
        };
        ui.show();

        initAssets();
        handleControlKeys();
        // Setting up the camera
        aspectRatio = 9f / 16f;//(float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        cam = new OrthographicCamera();
        cam.zoom = 1.2f;
        gameViewport = new ScreenViewport(cam);
        gameViewport.setUnitsPerPixel(unitScale);

        // Tiled map part
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);
        // Other
        Pixmap pix = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pix.setColor(Color.DARK_GRAY);
        pix.fillRectangle(12, 0, 8, 32);
        pix.fillRectangle(0, 12, 32, 8);
        pix.setColor(Color.RED);
        pix.drawRectangle(12, 0, 8, 32);
        pix.drawRectangle(0, 12, 32, 8);
        cursorTexture = new Texture(pix);
        pix.dispose();

        WorldManager.save(WorldManager.WorldGenerator.logarithmic(90, 90), "world.mm");

        cm = new ChunkManager("world.mm");
        for (int y = 0; y < chunksFieldSize; y++) {
            for (int x = 0; x < chunksFieldSize; x++) {
                chunksField[y][x] = new Chunk(new IntPair(30 + x, 30 + y), null);
            }
        }
        Chunk[] chunks = new Chunk[chunksFieldSize * chunksFieldSize];
        for (int i = 0; i < chunksFieldSize; i++) {
            for (int j = 0; j < chunksFieldSize; j++) {
                chunks[i * chunksFieldSize + j] = chunksField[i][j];
            }
        }
        cm.load(chunks);
        fieldpix = new Pixmap(chunksFieldSize * (int) Chunk.getSize(), chunksFieldSize * (int) Chunk.getSize(), Pixmap.Format.RGBA8888);
        fieldpix.setBlending(Pixmap.Blending.None);
        WorldRenderer wr = new WorldRenderer();
        fieldTextureSize = Chunk.getSize() * chunksFieldSize;
    }

    public void reloadChunks() {
        if ((int) cursorx / Chunk.getSize() != (int) oldcursorx / Chunk.getSize() || (int) cursory / Chunk.getSize() != (int) oldcursory / Chunk.getSize()) {
            for (int y = 0; y < chunksFieldSize; y++) {
                for (int x = 0; x < chunksFieldSize; x++) {
                    chunksField[y][x] = new Chunk(new IntPair(30 + (int) cursorx / Chunk.getSize() + x, 30 - (int) cursory / Chunk.getSize() + y), null);
                }
            }
            Chunk[] chunks = new Chunk[chunksFieldSize * chunksFieldSize];
            for (int i = 0; i < chunksFieldSize; i++) {
                for (int j = 0; j < chunksFieldSize; j++) {
                    chunks[i * chunksFieldSize + j] = chunksField[i][j];
                }
            }
            if (cursorx / Chunk.getSize() != oldcursorx / Chunk.getSize() || cursory / Chunk.getSize() != oldcursory / Chunk.getSize()) {
                long begin = System.nanoTime();
                cm.load(chunks);
                long end = System.nanoTime();
                //System.out.print("\rReload execution time: " + (end - begin) / 1000.0 / 1000.0 + " ms");
                oldcursorx = cursorx;
                oldcursory = cursory;
            }
        }
    }

    public void renderChunks() {
        long begin = System.nanoTime();
        int color = 0x889977FF, alpha;
        for (int cy = 0; cy < chunksFieldSize; cy++) {
            for (int cx = 0; cx < chunksFieldSize; cx++) {
                int cells[][] = chunksField[cy][cx].getCells();
                for (int y = 0; y < Chunk.getSize(); y++) {
                    for (int x = 0; x < Chunk.getSize(); x++) {
                        alpha = (cells[y][x] * 8) % 256;
                        fieldpix.drawPixel(x + Chunk.getSize() * cx, y + Chunk.getSize() * cy, color - alpha);
                    }
                }
            }
        }

        chunksFieldTexture = new Texture(fieldpix);
        sb.begin();

        sb.draw(chunksFieldTexture,
                -(chunksFieldSize * Chunk.getSize()) / 2 + ((int) cursorx / Chunk.getSize()) * Chunk.getSize(),
                -(chunksFieldSize * Chunk.getSize()) / 2 + ((int) cursory / Chunk.getSize()) * Chunk.getSize(),
                fieldTextureSize, fieldTextureSize);
        sb.draw(cursorTexture, cursorx, cursory, 1, 1);
        sb.end();
        long end = System.nanoTime();
        //System.out.print("\rRender execution time: " + (end - begin) / 1000.0 / 1000.0 + " ms");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ui.act(delta);
        handlePlayerControl(delta);
        // Rendering map
        mapRenderer.setView(cam);
        //mapRenderer.render();
        sb.begin();
        sb.end();
        reloadChunks();
        // Update camera
        cam.translate(cursorx - cam.position.x, cursory - cam.position.y);
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        renderChunks();
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
        // Method "hide" is called when you close app manually on desktop
        dispose();
    }

    @Override
    public void dispose() {
        ui.dispose();
        Gdx.input.setInputProcessor(null);
        sb.dispose();
        fieldpix.dispose();
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
                if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                    if (keycode == Input.Keys.ESCAPE) {
                        if (ui.stage.getKeyboardFocus() != null) ui.stage.setKeyboardFocus(null);
                        else {
                            game.setScreen(new MainMenuScreen(game));
                        }
                    }
                    if (ui.stage.getKeyboardFocus() == null) {
                        if (keycode == Input.Keys.UP) cam.translate(0f, 1f * cam.zoom);
                        if (keycode == Input.Keys.DOWN) cam.translate(0f, -1f * cam.zoom);
                        if (keycode == Input.Keys.LEFT) cam.translate(-1f * cam.zoom, 0f);
                        if (keycode == Input.Keys.RIGHT) cam.translate(1f * cam.zoom, 0f);
                        if (keycode == Input.Keys.UP || keycode == Input.Keys.DOWN || keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT) {
                            System.out.printf("Camera pos: %f X %f\n", cam.position.x, cam.position.y);
                        }
                        if ((keycode == Input.Keys.EQUALS || keycode == Input.Keys.RIGHT_BRACKET) && cam.zoom > 0.1f)
                            cam.zoom -= 0.1f;
                        if ((keycode == Input.Keys.MINUS || keycode == Input.Keys.LEFT_BRACKET) && cam.zoom < 3.0f)
                            cam.zoom += 0.1f;
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
                        if (keycode == Input.Keys.F2) ;
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

    public void handlePlayerControl(float delta) {
        float speed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 16 : 8;
        if (ui.stage.getKeyboardFocus() == null) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) cursory += speed * delta;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) cursorx -= speed * delta;
            if (Gdx.input.isKeyPressed(Input.Keys.S)) cursory -= speed * delta;
            if (Gdx.input.isKeyPressed(Input.Keys.D)) cursorx += speed * delta;
        }
        float tpadX = ui.touchpad.getKnobPercentX();
        float tpadY = ui.touchpad.getKnobPercentY();
        if (0.3 < tpadY && tpadY < 0.9) cursory += 8 * delta;
        else if (tpadY > 0.9) cursory += 16 * delta;

        if (-0.9 < tpadX && tpadX < -0.3) cursorx -= 8 * delta;
        else if (tpadX < -0.9) cursorx -= 16 * delta;

        if (-0.9 < tpadY && tpadY < -0.3) cursory -= 8 * delta;
        else if (tpadY < -0.9) cursory -= 16 * delta;

        if (0.3 < tpadX && tpadX < 0.9) cursorx += 8 * delta;
        else if (tpadX > 0.9)
            cursorx += 16 * delta; // TODO: Debug cursor speed and position. Make a special class/scene2d.ui widget for debug info
    }
}