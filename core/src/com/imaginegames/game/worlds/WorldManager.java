package com.imaginegames.game.worlds;

import com.badlogic.gdx.Gdx;
import com.imaginegames.game.utils.math.MatrixGenerator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class WorldManager {

    public WorldManager() { }

    static public boolean save(World world, String worldFileName) {
        int[][] blocks = world.getCells();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(Gdx.files.local("localAssets/worlds/" + worldFileName).file()))) {
            dos.writeInt(world.getWidth());
            dos.writeInt(world.getHeight());
            for (int i = 0; i < world.getHeight(); i++) {
                for (int j = 0; j < world.getWidth(); j++) {
                    dos.writeInt(blocks[i][j]);
                }
            }
            return true;
        }
        catch (IOException ex) {
            System.err.println("An exception has occurred during saving world: " + ex.toString() + " :: " + ex.getMessage());
            return false;
        }
    }

    static public World load(String worldFileName) { // FIXME: 16.08.2021 Migrate to loading world regions, not the whole world
        int worldWidth, worldHeight;
        int[][] blocks;
        try (DataInputStream dis = new DataInputStream(new FileInputStream(Gdx.files.local("localAssets/worlds/" + worldFileName).file()))) {
            worldWidth = dis.readInt();
            worldHeight = dis.readInt();
            blocks = new int[worldHeight][worldWidth];
            for (int i = 0; i < worldHeight; i++) {
                for (int j = 0; j < worldWidth; j++) {
                    blocks[i][j] = dis.readInt();
                }
            }
            return new World(blocks);
        }
        catch (IOException ex) {
            System.err.println("An exception has occurred during loading world: " + ex.toString() + " :: " + ex.getMessage());
            return null;
        }
    }

    static public void synopsis(World world, boolean briefly) {
        try (FileWriter fileWriter = new FileWriter(Gdx.files.local("localAssets/worlds/printed worlds/printed.txt").file())) {
            fileWriter.write("w: " + world.getWidth() + ", h: " + world.getHeight() + "\n");
            if (!briefly) {
                for (int i = 0; i < world.getHeight(); i++) {
                    for (int j = 0; j < world.getWidth(); j++) {
                        fileWriter.write((world.getCells()[i][j]) + ", ");
                    }
                    fileWriter.write('\n');
                }
            }
        }
        catch (IOException ex) {
            System.err.println("An exception has occurred during printing world: " + ex.toString() + " :: " + ex.getMessage());
        }
    }

    public static class WorldGenerator {

        public WorldGenerator() { }

        public static World random(int width, int height) {
            return new World(MatrixGenerator.random(width, height));
        }

        public static World linear(int width, int height) {
            return new World(MatrixGenerator.linear(width, height));
        }

        public static World logarithmic(int width, int height) {
            return new World(MatrixGenerator.logarithmic(width, height));
        }
    }
}
