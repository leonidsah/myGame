package com.imaginegames.game.screens.worldmanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.imaginegames.game.MyGameMain;
import com.imaginegames.game.Values;
import com.imaginegames.game.screens.game.GameScreen;
import com.imaginegames.game.screens.mainmenu.MainMenuScreen;
import com.imaginegames.game.worlds.ProceduralWorld;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class WorldManagerScreen implements com.badlogic.gdx.Screen {
    private MyGameMain game;
    private UI ui;

    public WorldManagerScreen(MyGameMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        /*int a = -8;
        for (int i = 0; i < 9; i++) {
            System.out.println((a + i) + "% 8 = " + (a + i) % 8);
        }*/
        ui = new UI(game) {
            @Override
            void externalShow() {
                returnToMainMenuButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        game.setScreen(new MainMenuScreen(game));
                    }
                });
                toggleLoadSectionButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        worldsList.setItems(Gdx.files.external(Values.WORLDS_DIR_PATH).list());
                    }
                });
                proceedCreateButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        String worldName = worldNameTextField.getText();
                        String seedString = seedTextField.getText();
                        String resultMessage;
                        long seed = seedString.equals("") ? 0 : Long.parseLong(seedString);
                        ProceduralWorld world = new ProceduralWorld(worldName, seed, (byte)8);
                        int result = writeWorldToExternalStorage(world);
                        if (result == 0) resultMessage = "World creation complete";
                        else if (result == 2) resultMessage = "World creation failed: world already exists";
                        else resultMessage = "World creation failed: error code " + result;
                        logChat.handleConsoleMessage(resultMessage);
                    }
                });
                proceedLoadButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        FileHandle selectedWorld = worldsList.getSelected();
                        if (selectedWorld == null) return;
                        logChat.handleConsoleMessage("Proceeding with " + selectedWorld.toString() + "...");
                        ProceduralWorld world = readWorldFromExternalStorage(selectedWorld);
                        if (world == null) System.err.println("Failed");
                        else {
                            System.out.println(world.getName() + " has been read!");
                            long seed = world.getSeed();
                            logChat.handleConsoleMessage("This world's seed is: " + seed);
                            logChat.handleConsoleMessage("This world's filehandle is: " + world.getDirectory());
                            game.setScreen(new GameScreen(game, world));
                        }
                    }
                });
            }
        };
        ui.show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ui.act(delta);
        ui.draw();
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
        dispose();
    }

    @Override
    public void dispose() {
        ui.dispose();
    }

    /** 0 - success, non-0 - error  **/
    public int writeWorldToExternalStorage(ProceduralWorld world) {
        FileHandle worldsDir = Gdx.files.external(Values.WORLDS_DIR_PATH);
        FileHandle worldDir, dataFile;
        String worldName = world.getName();
        long seed = world.getSeed();

        // Check if directory exists, where worlds' directories should be
        boolean exists = worldsDir.exists();
        if (!exists) {
            worldsDir.mkdirs();
            exists = worldsDir.exists();
            if (!exists) return 1;
        }

        // Create world's directory
        worldDir = worldsDir.child(worldName);
        if (worldDir.exists()) return 2;
        worldDir.mkdirs();
        if (worldDir.exists()) {
            dataFile = worldDir.child("data");
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(dataFile.write(false)))) {
                oos.writeObject(world);
            } catch (Exception e) {
                System.err.println("An exception has occurred: " + e);
                return 4;
            }
            return 0;
        }
        else return 3;
    }

    public ProceduralWorld readWorldFromExternalStorage(FileHandle worldDir) {
        FileHandle dataFile = worldDir.child("data");
        ProceduralWorld world = null;
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(dataFile.read()))) {
            world = (ProceduralWorld) ois.readObject();
        }
        catch (Exception e) { System.err.println("An exception has occurred: " + e); }
        finally { return world; }
    }
}
