package com.imaginegames.game.worlds;

import com.badlogic.gdx.Gdx;
import com.imaginegames.game.utils.math.IntPair;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import static java.lang.Math.abs;

public class ChunkManager {
    private String worldFileName;
    private DataInputStream dis;
    private int worldWidth, worldHeight, worldCWidth, worldCHeight;

    public ChunkManager(String worldFileName) {
        this.worldFileName = worldFileName;
    }

    // Returns true if loading is successful (e.g. no exceptions have occurred during reading file)
    public boolean load(Chunk[] chunks) {
        int worldWidth, worldHeight, cellCount = 0, cellX = 0, cellY = 0, val;
        HashMap<IntPair, Chunk> chunkMap = new HashMap<>();
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(Gdx.files.local("localAssets/worlds/" + worldFileName).file())))) {
            worldWidth = dis.readInt();
            worldHeight = dis.readInt();
            worldCWidth = worldWidth / Chunk.getSize();
            worldCHeight = worldWidth / Chunk.getSize();

            if (worldWidth <= 0 || worldHeight <= 0) {
                throw new IllegalArgumentException("Wrong/corrupted world file: WorldWidth or WorldHeight was read as value which is <= 0");
            }
            for (Chunk c : chunks) {
                int x = c.getXY().getX() >= 0 ? c.getXY().getX() % (worldCWidth) :
                        (worldCWidth - (abs(c.getXY().getX() % (worldCWidth)))) % worldCWidth;
                int y = c.getXY().getY() >= 0 ? c.getXY().getY() % (worldCHeight) :
                        (worldCHeight - (abs(c.getXY().getY() % (worldCHeight)))) % worldCWidth;
                chunkMap.put(new IntPair(x, y), c);
            }

            while (dis.available() > 0) {
                val = dis.readInt();
                Chunk c = chunkMap.get(new IntPair(cellX / Chunk.getSize(), cellY / Chunk.getSize()));
                if (c != null) {
                    int[][] cells = c.getCells();
                    cells[cellX % Chunk.getSize()][cellY % Chunk.getSize()] = val;
                }
                cellCount++;
                cellX = cellCount % worldWidth;
                cellY = cellCount / worldWidth;
            }
            return true;
        }
        catch (IOException e) {
            System.err.printf("An exception has occurred in ChunkManager: %s :: %s", e.toString(), e.getMessage());
            return false;
        }
    }
    // public void saveChunks(World world, Chunk[] chunks) { }
}
