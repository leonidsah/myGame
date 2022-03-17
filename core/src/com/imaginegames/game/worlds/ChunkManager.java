package com.imaginegames.game.worlds;

import com.badlogic.gdx.files.FileHandle;
import com.imaginegames.game.utils.math.IntPair;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Set;

public class ChunkManager {
    FileHandle worldDir;
    long seed;
    byte chunkSize;
    byte chunkFieldWidth, chunkFieldHeight;
    Chunk[][] chunkField;
    HashMap<IntPair, Chunk> markMap = new HashMap<>();
    int value = 0;


    public ChunkManager(ProceduralWorld world, byte chunkFieldWidth, byte chunkFieldHeight) {
        this.chunkFieldWidth = chunkFieldWidth;
        this.chunkFieldHeight = chunkFieldHeight;
        chunkField = new Chunk[chunkFieldWidth][chunkFieldHeight];
        worldDir = world.getDirectory();
        seed = world.getSeed();
        chunkSize = world.getChunkSize();
    }
    
    public Chunk[][] getChunkField() { return chunkField; }

    /** ChunkField coordinates are the coordinates of the it's center chunk */
    public void updateChunkField(int chunkFieldX, int chunkFieldY) {
        chunkFieldX -= chunkFieldWidth / 2;
        chunkFieldY -= chunkFieldHeight / 2;
        int i, j;
        for (i = 0; i < chunkFieldWidth; i++)
            for (j = 0; j < chunkFieldHeight; j++) {
                chunkField[i][j] = getChunk(chunkFieldX + i, chunkFieldY + j);
            }
    }
    
    Chunk getChunk(int chunkX, int chunkY) {
        Chunk chunk;
        if (chunkX == 0 && chunkY == 0) {
            chunk = new Chunk(chunkSize);
            int[][] cells = new int[chunkSize][chunkSize];
            for (int i = 0; i < chunkSize; i++)
                for (int j = 0; j < chunkSize; j++) {
                    value = 200;
                    chunk.setCell(i, j, value);
                }

        }
        else {
            chunk = new Chunk(chunkSize);
            int[][] cells = new int[chunkSize][chunkSize];
            for (int i = 0; i < chunkSize; i++)
                for (int j = 0; j < chunkSize; j++) {
                    value = (value + 2) % 256;
                    chunk.setCell(i, j, value);
                }
        }
        /*if (isSaved(chunkX, chunkY)) {
            chunk = loadChunk(chunkX, chunkY);
        }
        else {
            chunk = ChunkGenerator.generateChunk(seed, chunkSize);
            markToSave(chunkX, chunkY, chunk);
        }*/
        return chunk;
    }

    public void setCell(int x, int y, int value) {
        int chunkX, chunkY, cellX, cellY;
        x -= (x >= 0 ? 0 : chunkSize);
        y -= (y >= 0 ? 0 : chunkSize);
        chunkX = x / chunkSize;
        chunkY = y / chunkSize;
        cellX = x % chunkSize;
        cellY = y % chunkSize;
        
        chunkField[chunkX][chunkY].setCell(cellX, cellY, value);
        markToSave(chunkX, chunkY, chunkField[chunkX][chunkY]);
    }

    void markToSave(int chunkX, int chunkY, Chunk chunk) {
        IntPair coords = new IntPair(chunkX, chunkY);
        if (markMap.containsKey(coords)) markMap.remove(coords);
        else markMap.put(coords, chunk);
        markMap.put(coords, chunk);
    }
    
    boolean isSaved(int chunkX, int chunkY) {
        return worldDir.child(chunkX + "_" + chunkY).exists();
    }
    
    void saveAll() {
        Set<IntPair> keySet = markMap.keySet();
        for (IntPair chunkCoords: keySet) {
            FileHandle dataFile = worldDir.child(chunkCoords.getX() + "_" + chunkCoords.getY());
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(dataFile.write(false)))) {
                oos.writeObject(markMap.get(chunkCoords));
            } catch (Exception e) {
                System.err.println("An exception has occurred has occurred during write: " + e);
            }
        }
    }
    
    Chunk loadChunk(int chunkX, int chunkY) {
        FileHandle dataFile = worldDir.child(chunkX + "_" + chunkY);
        Chunk chunk = null;
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(dataFile.read()))) {
            chunk = (Chunk) ois.readObject();
        } 
        catch (Exception e) { System.err.println("An exception has occurred during read: " + e); }
        finally { return chunk; }
    }

    public byte getChunkSize() {
        return chunkSize;
    }

    public byte getChunkFieldWidth() {
        return chunkFieldWidth;
    }

    public byte getChunkFieldHeight() {
        return chunkFieldHeight;
    }
}
