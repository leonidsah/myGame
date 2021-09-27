package com.imaginegames.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.imaginegames.game.MilitaryMadnessMain;
import com.imaginegames.game.Values;

public class MainMenuScreen implements Screen {
    private MilitaryMadnessMain game;
    private float stateTime;
    private Animation<?>[] rolls;
    private BitmapFont font, font_s, font_info, font_colored;
    private GlyphLayout version, fpsText, welcomeTitle;
    private Skin skin;
    private Stage stage;
    private ScreenViewport screenViewport;
    private Table rootTable, leftTable, centerTable, rightTable;
    private TextButton playButton, exitButton, settingsButton, showDebugDialogButton;
    private Dialog debugDialog;
    private boolean showSettings = false;
    private CheckBox debugModeCheckBox, fullscreenModeCheckbox;
    private Music music1;
    private float textButtonScale = Values.mainMenuScreenUIScale;
    private float checkBoxScale = Values.mainMenuScreenUIScale / 2.0f;
    

    public MainMenuScreen(MilitaryMadnessMain game) {
        this.game = game;
        game.interface_batch = new SpriteBatch();
    }

    @Override
    public void show() {
        initUI();
        // Music
        music1 = game.assets.get("music/Wind, winter and cold.mp3");
        music1.setVolume(0.05f);
        music1.play();
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) enableHotkeys();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        resizeUI(width, height);
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

    public void initUI() {
        // Setting up a stage
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
        fullscreenModeCheckbox = new CheckBox("Fullscreen", skin);
        showDebugDialogButton = new TextButton("Debug info", skin);
        debugModeCheckBox.setChecked(Values.stagesDebug);
        debugDialog = new Dialog("Debug info dialog", skin) {
            @Override
            protected void result(Object object) {
                super.result(object);
            }
        };
        debugDialog.button("Got it");
        debugDialog.setColor(Color.GRAY);
        // Adding listeners to scene2d.ui actors
        showDebugDialogButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                debugDialog.getContentTable().reset();
                String text = "The Java heap memory use in MB: " +
                        (Gdx.app.getJavaHeap() / (1024 * 1024)) + "\nGame version: " + Values.VERSION + " (libGDX version: " + Version.VERSION +
                        ")\nAPI version: " + Gdx.app.getVersion();
                debugDialog.text(text);
                debugDialog.show(stage);
            }
        });
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSettings = !showSettings;
                leftTable.setVisible(showSettings);
                rightTable.setVisible(showSettings);
                super.clicked(event, x, y);
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                exitGame();
            }
        });
        debugModeCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Values.stagesDebug = debugModeCheckBox.isChecked();
                stage.setDebugAll(Values.stagesDebug);
                super.clicked(event, x, y);
            }
        });
        fullscreenModeCheckbox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Values.fullscreenMode = fullscreenModeCheckbox.isChecked();
                if (Values.fullscreenMode) Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                else Gdx.graphics.setWindowedMode(1280, 720);
                super.clicked(event, x, y);
            }
        });
    }

    // (Re-)Sizing and (re-)adding actors to root table
    private void resizeUI(float width, float height) {
        //System.out.println("width: " + width + " height: " + height);
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
        rebuildTable();
    }

    private void rebuildTable() {
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
        leftTable.add(debugModeCheckBox);
        rightTable.add(showDebugDialogButton).right().prefHeight(64f);
        // Desktop UI part
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            fullscreenModeCheckbox.setChecked(Values.fullscreenMode);
            leftTable.row();
            leftTable.add(fullscreenModeCheckbox);
        }
    }

    public void enableHotkeys() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.D) {
                    Values.stagesDebug = !Values.stagesDebug;
                    stage.setDebugAll(Values.stagesDebug);
                }
                if (keycode == Input.Keys.Q) {
                    exitGame();
                }
                if (keycode == Input.Keys.ESCAPE) {
                    game.setScreen(new GameScreen(game));
                    dispose();
                }
                if (keycode == Input.Keys.F) {
                    Values.fullscreenMode = !Values.fullscreenMode;
                    fullscreenModeCheckbox.setChecked(Values.fullscreenMode);
                    if (Values.fullscreenMode) Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    else Gdx.graphics.setWindowedMode(1280, 720);
                }
                return super.keyDown(event, keycode);
            }
        });
    }

    public void exitGame() {
        dispose();
        game.assets.dispose();
        Gdx.app.exit();
    }
}

