package com.imaginegames.game.worlds;

import com.badlogic.gdx.Gdx;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WorldManager {

    public WorldManager() { }

    public void saveChunks(World world, Chunk[] chunks) {

    }

    static public Chunk[] loadChunks(String worldFileName) {
        int worldWidth, worldHeight;
        int[][] blocks;
        try (DataInputStream dis = new DataInputStream(new FileInputStream(Gdx.files.local("localAssets/worlds/" + worldFileName).file()))) {
            worldWidth = dis.readInt();
            worldHeight = dis.readInt();
            blocks = new int[worldHeight][worldWidth];
            dis.readChar();
            for (int i = 0; i < worldHeight; i++) {
                for (int j = 0; j < worldWidth; j++) {
                    blocks[i][j] = dis.readInt();
                }
                dis.readChar();
            }
            //return new World(blocks);
            return null;

        }
        catch (IOException ex) {
            System.err.println("An exception has occurred during loading world: " + ex.toString() + " :: " + ex.getMessage());
            return null;
        }
    }

    static public boolean saveWorld(World world, String worldFileName) {
        int[][] blocks = world.getBlocks();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(Gdx.files.local("localAssets/worlds/" + worldFileName).file()))) {
            dos.writeInt(world.getWorldWidth());
            dos.writeInt(world.getWorldHeight());
            dos.writeChar('\n');
            for (int i = 0; i < world.getWorldHeight(); i++) {
                for (int j = 0; j < world.getWorldWidth(); j++) {
                    dos.writeInt(blocks[i][j]);
                }
                dos.writeChar('\n');
            }
            return true;
        }
        catch (IOException ex) {
            System.err.println("An exception has occurred during saving world: " + ex.toString() + " :: " + ex.getMessage());
            return false;
        }
    }

    static public World loadWorld(String worldFileName) { // FIXME: 16.08.2021 Migrate to loading world regions, not the whole world
        int worldWidth, worldHeight;
        int[][] blocks;
        try (DataInputStream dis = new DataInputStream(new FileInputStream(Gdx.files.local("localAssets/worlds/" + worldFileName).file()))) {
            worldWidth = dis.readInt();
            worldHeight = dis.readInt();
            blocks = new int[worldHeight][worldWidth];
            dis.readChar();
            for (int i = 0; i < worldHeight; i++) {
                for (int j = 0; j < worldWidth; j++) {
                    blocks[i][j] = dis.readInt();
                }
                dis.readChar();
            }
            return new World(blocks);
        }
        catch (IOException ex) {
            System.err.println("An exception has occurred during loading world: " + ex.toString() + " :: " + ex.getMessage());
            return null;
        }
    }

    public void printWorld(World world, boolean briefly) {
        try (FileWriter fileWriter = new FileWriter(Gdx.files.local("localAssets/worlds/printed worlds/printed.txt").file())) {
            fileWriter.write("w: " + world.getWorldWidth() + ", h: " + world.getWorldHeight() + "\n");
            if (!briefly) {
                for (int i = 0; i < world.getWorldHeight(); i++) {
                    for (int j = 0; j < world.getWorldWidth(); j++) {
                        fileWriter.write((world.getBlocks()[i][j]) + ", ");
                    }
                    fileWriter.write('\n');
                }
            }
        }
        catch (IOException ex) {
            System.err.println("An exception has occurred during printing world: " + ex.toString() + " :: " + ex.getMessage());
        }
    }
}
