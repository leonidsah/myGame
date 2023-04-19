package com.imaginegames.game.screens.mainmenu;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.imaginegames.game.MyGameMain;
import com.imaginegames.game.Values;
import com.imaginegames.game.screens.game.GameScreen;
import com.imaginegames.game.screens.worldmanager.WorldManagerScreen;
import com.imaginegames.game.utils.SettingsPreferences;

public class MainMenuScreen implements com.badlogic.gdx.Screen {
    private MyGameMain game;
    private UI ui;
    private float stateTime;
    private Animation<?>[] rolls;
    private BitmapFont font, font_s, font_info, font_colored;
    private GlyphLayout version, fpsText, welcomeTitle;
    private Music music1;
    private boolean performExit = false;

    public MainMenuScreen(MyGameMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        ui = new UI(game) {
            @Override
            void externalShow() {
                showDebugDialogButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        debugDialog.getContentTable().reset();
                        String text = "The Java heap memory use in MB: " +
                                (Gdx.app.getJavaHeap() / (1024 * 1024)) + "\nGame version: " + Values.VERSION + " " + Values.WELCOME_TITLE + "\nlibGDX version: " + Version.VERSION +
                                "\nAPI version: " + Gdx.app.getVersion();
                        debugDialog.text(text);
                        debugDialog.show(stage);
                    }
                });
                playButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        dispose();
                        game.setScreen(new WorldManagerScreen(game));
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
                        performExit = true;
                    }
                });
                debugModeCheckBox.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Values.debugMode = debugModeCheckBox.isChecked();
                        stage.setDebugAll(Values.debugMode);
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
                logFPSCheckBox.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Values.logFPS = logFPSCheckBox.isChecked();
                        super.clicked(event, x, y);
                    }
                });
                if (Gdx.app.getType() == Application.ApplicationType.Desktop) enableHotkeys();
            }
        };
        ui.show();

        // Music
        music1 = game.assets.get("music/Wind, winter and cold.mp3");
        music1.setVolume(0.05f);
        if (Values.playMusic) music1.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ui.act(delta);
        ui.draw();
        if (performExit) disposeAndExit();
    }

    @Override
    public void resize(int width, int height) {
        ui.resize(width, height);
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
        ui.dispose();
    }

    public void enableHotkeys() {
        ui.stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.D) {
                    Values.debugMode = !Values.debugMode;
                    ui.stage.setDebugAll(Values.debugMode);
                }
                if (keycode == Input.Keys.Q) {
                    performExit = true;
                }
                if (keycode == Input.Keys.F) {
                    Values.fullscreenMode = !Values.fullscreenMode;
                    ui.fullscreenModeCheckbox.setChecked(Values.fullscreenMode);
                    if (Values.fullscreenMode) Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    else Gdx.graphics.setWindowedMode(1280, 720);
                }
                return super.keyDown(event, keycode);
            }
        });
    }

    public void disposeAndExit() {
        game.setScreen(null);
        dispose();
        game.assets.dispose();
        SettingsPreferences.save();
        Gdx.app.exit();
    }
}