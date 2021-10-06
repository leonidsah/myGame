package com.imaginegames.game.screens.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.imaginegames.game.MilitaryMadnessMain;
import com.imaginegames.game.Values;
import com.imaginegames.game.screens.mainmenu.MainMenuScreen;
import com.imaginegames.game.screens.game.GameScreen;

import static java.lang.Math.round;

public class LoadingScreen implements Screen {

    private MilitaryMadnessMain game;
    private UI ui;
    private String[] assetsArray;

    public LoadingScreen(MilitaryMadnessMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        ui = new UI(game) {
            @Override
            void external() {

            }
        };
        ui.show();

        // Assets loading
        assetsArray = new String[]
                {
                        // Skins
                        Values.SKIN_PATH,
                        // Textures
                        "textures/loadingScreenLogo.png",
                        "textures/pack.atlas",
                        // Sounds
                        "sounds/go_sound.mp3",
                        "sounds/cancel_sound.mp3",
                        // Music
                        "music/Wind, winter and cold.mp3",
                        // Tiled maps
                        "tiledMaps/tiledMap.tmx",
                        // Fonts
                        "fonts/luxi.fnt"
                };

        // Loading assets (including assets for loading screen)
        game.assets.load("textures/loadingScreenLogo.png", Texture.class);
        game.assets.load("textures/pack.atlas", TextureAtlas.class);
        for (String asset : assetsArray) {
            if (asset.contains("skins/")) game.assets.load(asset, Skin.class);
            else if (asset.contains("textures/")) {
                if (asset.matches(".+\\.atlas")) game.assets.load(asset, TextureAtlas.class);
                else game.assets.load(asset, Texture.class);
            }
            else if (asset.contains("sounds/")) game.assets.load(asset, Sound.class);
            else if (asset.contains("music/")) game.assets.load(asset, Music.class);
            else if (asset.contains("tiledMaps")) {
                game.assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
                game.assets.load(asset, TiledMap.class);
            }
            else if (asset.contains("fonts/")) {
                game.assets.load(asset, BitmapFont.class);
            }
        }
        /* Template for loading fonts using asset manager
        FileHandleResolver resolver = new InternalFileHandleResolver();
        game.assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        game.assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter font = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        font.fontFileName = "fonts/Play-Bold.ttf";
        font.fontParameters.characters = Values.DEFAULT_CHARS;
        font.fontParameters.size = 64;
        font.fontParameters.spaceX = -3;
        font.fontParameters.borderWidth = 3;
        font.fontParameters.borderColor = new com.badlogic.gdx.graphics.Color(0f, 0f, 0f, 0f);
        font.fontParameters.color = new com.badlogic.gdx.graphics.Color(0f, 0f, 0f, 1f);
        game.assets.load("fonts/Play-Bold.ttf", BitmapFont.class, font);*/
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.312f, 0.312f, 0.312f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        boolean isFinished = game.assets.update();
        ui.act(delta);
        ui.draw();
        if (isFinished) {
            game.assets.finishLoading();
            if (!Values.stayOnLoadingScreen) {
                dispose();
                if (!Values.skipMainMenuScreen) game.setScreen(new MainMenuScreen(game));
                else game.setScreen(new GameScreen(game));
            }
        } else ui.loadingTime += delta;
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
        game.assets.unload("textures/loadingScreenLogo.png");
    }
}