package com.imaginegames.game.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class World {
    private int worldWidth;
    private int worldHeight;
    private int[][] blocks;

    // Creates a random world
    public World() {
        World world = WorldGenerator.generateRandom();
        worldWidth = world.getWorldWidth();
        worldHeight = world.getWorldHeight();
        blocks = world.getBlocks();
    }

    public World(int[][] blocks) {
        this.blocks = blocks;
        this.worldHeight = blocks.length;
        this.worldWidth = blocks[0].length;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public int[][] getBlocks() {
        return blocks;
    }
}
