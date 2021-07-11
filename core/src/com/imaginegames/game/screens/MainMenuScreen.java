package com.imaginegames.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.imaginegames.game.MilitaryMadnessMain;
import com.imaginegames.game.utils.Values;

public class MainMenuScreen implements Screen {

	private MilitaryMadnessMain game;
	private float stateTime;
	private Animation<?>[] rolls;
	private BitmapFont font, font_s, font_info, font_colored;
	private GlyphLayout version, fpsText, welcomeTitle;
	private Skin skin;
	private Stage stage;
	private Table rootTable, leftTable, centerTable, rightTable;
	private TextButton playButton, exitButton, settingsButton, showDebugDialogButton;
	private Dialog debugDialog;
	private boolean showSettings = false;
	private CheckBox debugModeCheckBox;
	private Music music1;


	public MainMenuScreen(MilitaryMadnessMain game) {
		this.game = game;
		game.interface_batch = new SpriteBatch();
	}

	@Override
	public void show() {
		//Preset
		font = game.assets.get("fonts/Play-Bold.ttf", BitmapFont.class);
		font_s = game.assets.get("fonts/Play-Bold_S.ttf", BitmapFont.class);
		font_info = game.assets.get("fonts/Play-Regular_Info.ttf", BitmapFont.class);
		font_colored = game.assets.get("fonts/Play-Regular_Colored.ttf", BitmapFont.class);
		skin = game.assets.get(Values.SKIN_PATH, Skin.class);
		// Setting up a stage
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		stage.setDebugAll(Values.STAGES_DEBUG);
		rootTable = new Table();
		stage.addActor(rootTable);
		rootTable.setFillParent(true);
		// Setting up widgets (buttons and etc.)
		centerTable = new Table();
		leftTable = new Table();
		rightTable = new Table();
		playButton = new TextButton("Play", skin);
		settingsButton = new TextButton("Settings", skin);
		exitButton = new TextButton("Exit", skin);
		debugModeCheckBox = new CheckBox("Debug mode", skin);
		showDebugDialogButton = new TextButton("Debug info", skin);
		debugModeCheckBox.setChecked(Values.STAGES_DEBUG);
		playButton.getLabel().setFontScale(Values.UIScale);
		settingsButton.getLabel().setFontScale(Values.UIScale);
		exitButton.getLabel().setFontScale(Values.UIScale);
		debugDialog = new Dialog("Debug info dialog", skin) {
			@Override
			protected void result(Object object) {
				super.result(object);
			}
		};
		debugDialog.button("Got it");
		debugDialog.setColor(Color.GRAY);

		centerTable.add(playButton).row();
		centerTable.add(settingsButton).space(40f).row();
		centerTable.add(exitButton).row();
		leftTable.center();
		leftTable.setVisible(showSettings);
		rightTable.setVisible(showSettings);
		debugModeCheckBox.getLabel().setFontScale(1.5f);

		rootTable.add(leftTable).fill().prefWidth(400f).pad(10f).uniform();
		rootTable.add(centerTable).expand();
		rootTable.add(rightTable).fill().pad(10f).uniform();
		leftTable.add(debugModeCheckBox);
		rightTable.add(showDebugDialogButton).right().prefHeight(64f);
		showDebugDialogButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				debugDialog.getContentTable().reset();
				debugDialog.text("It's a test dialog\nThe Java heap memory use in MB: " + (Gdx.app.getJavaHeap() / (1024 * 1024)));
				debugDialog.show(stage);
			}
		});
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.setScreen(new GameScreen(game));
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
				dispose();
				Gdx.app.exit();
			}
		});
		debugModeCheckBox.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Values.STAGES_DEBUG = debugModeCheckBox.isChecked();
				stage.setDebugAll(Values.STAGES_DEBUG);
				super.clicked(event, x, y);
			}
		});
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

	public void enableHotkeys() {
		stage.addListener(new InputListener() {
			@Override
			public boolean keyTyped(InputEvent event, char character) {
				if (event.getKeyCode() == Input.Keys.D) {
					Values.STAGES_DEBUG = !Values.STAGES_DEBUG;
					stage.setDebugAll(Values.STAGES_DEBUG);
				}
				if (event.getKeyCode() == Input.Keys.Q) {
					dispose();
					Gdx.app.exit();
				}
				return super.keyTyped(event, character);
			}
		});
	}
}

