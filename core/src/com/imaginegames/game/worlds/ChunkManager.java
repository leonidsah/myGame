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
    private Chunk[][] chunkField;
    private HashMap<IntPair, Chunk> chunksToSave = new HashMap<>();
    private byte chunkFieldWidth, chunkFieldHeight;
    private FileHandle worldDir;
    private long seed;
    private byte chunkSize;
    int value = 0;
    int chunkFieldX, chunkFieldY;
    int centerChunkX, centerChunkY;

    public ChunkManager(ProceduralWorld world, byte chunkFieldWidth, byte chunkFieldHeight) {
        this.chunkFieldWidth = chunkFieldWidth;
        this.chunkFieldHeight = chunkFieldHeight;
        chunkField = new Chunk[chunkFieldWidth][chunkFieldHeight];
        worldDir = world.getDirectory();
        seed = world.getSeed();
        chunkSize = world.getChunkSize();
    }

    /** ChunkField coordinates are the coordinates of the it's center chunk */
    public void updateChunkField(int chunkFieldX, int chunkFieldY) {
        this.chunkFieldX = chunkFieldX;
        this.chunkFieldY = chunkFieldY;
        centerChunkX = chunkFieldX;
        centerChunkY = chunkFieldY;
        chunkFieldX -= chunkFieldWidth / 2;
        chunkFieldY -= chunkFieldHeight / 2;

        int i, j;
        for (i = 0; i < chunkFieldWidth; i++)
            for (j = 0; j < chunkFieldHeight; j++) {
                chunkField[i][j] = getChunk(chunkFieldX + i, chunkFieldY + j);
            }
    }

    public void setCell(float x, float y, int value) {
        int chunkX, chunkY, cellX, cellY;
        int leftBottomChunkX, leftBottomChunkY, rightTopX, rightTopY;
        leftBottomChunkX = centerChunkX - chunkFieldWidth / 2;
        leftBottomChunkY = centerChunkY - chunkFieldHeight / 2;
        rightTopX = centerChunkX + (chunkFieldWidth - chunkFieldWidth / 2) - 1;
        rightTopY = centerChunkY + (chunkFieldHeight - chunkFieldHeight / 2) - 1;
        chunkX = Chunk.getChunkCoord(x, chunkSize);
        chunkY = Chunk.getChunkCoord(y, chunkSize);

        if (chunkX < leftBottomChunkX || chunkX > rightTopX) return;
        if (chunkY < leftBottomChunkY || chunkY > rightTopY) return;

        cellX = Chunk.getCellCoord(x, chunkSize);
        cellY = Chunk.getCellCoord(y, chunkSize);

        chunkField[chunkX - leftBottomChunkX][chunkY - leftBottomChunkY].setCell(cellX, cellY, value);
        markToSave(chunkX, chunkY, chunkField[chunkX - leftBottomChunkX][chunkY - leftBottomChunkY]);
    }
    
    private Chunk getChunk(int chunkX, int chunkY) {
        Chunk chunk;
        // If chunk was modified - then load it
        if (isModified(chunkX, chunkY)) {
            //System.out.println("chunkX = " + chunkX + "    chunkY = " + chunkY + " isModified: " + isModified(chunkX, chunkY));
            return chunksToSave.get(new IntPair(chunkX, chunkY));
        }
        // Try to load chunk from the disk
        else if (isSaved(chunkX, chunkY)) {
            chunk = loadChunk(chunkX, chunkY);
        }
        // Mark chunk with coords (0, 0)
        else if (chunkX == 0 && chunkY == 0) {
            chunk = new Chunk(chunkSize);
            int[][] cells = new int[chunkSize][chunkSize];
            for (int i = 0; i < chunkSize; i++)
                for (int j = 0; j < chunkSize; j++) {
                    value = 50;
                    chunk.setCell(i, j, value);
                }
        }
        // If chunk has never been generated - then generate it
        else {
            // Do not use setCell method here, it leads to buggy recursion things
            chunk = ChunkGenerator.generateChunk(chunkX, chunkY, seed, chunkSize);
        }
        return chunk;
    }

    private void markToSave(int chunkX, int chunkY, Chunk chunk) {
        IntPair coords = new IntPair(chunkX, chunkY);
        if (chunksToSave.containsKey(coords)) chunksToSave.remove(coords);
        else chunksToSave.put(coords, chunk);
        chunksToSave.put(coords, chunk);
    }
    
    private boolean isSaved(int chunkX, int chunkY) {
        return worldDir.child(chunkX + "_" + chunkY).exists();
    }

    private boolean isModified(int chunkX, int chunkY) {
        return chunksToSave.containsKey(new IntPair(chunkX, chunkY));
    }
    
    public void saveChunks() {
        Set<IntPair> keySet = chunksToSave.keySet();
        for (IntPair chunkCoords: keySet) {
            FileHandle dataFile = worldDir.child(chunkCoords.getX() + "_" + chunkCoords.getY());
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(dataFile.write(false)))) {
                oos.writeObject(chunksToSave.get(chunkCoords));
            } catch (Exception e) {
                System.err.println("An exception has occurred has occurred during writing: " + e);
            }
        }
    }

    private Chunk loadChunk(int chunkX, int chunkY) {
        FileHandle dataFile = worldDir.child(chunkX + "_" + chunkY);
        Chunk chunk = null;
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(dataFile.read()))) {
            chunk = (Chunk) ois.readObject();
        }
        catch (Exception e) { System.err.println("An exception has occurred during reading: " + e); }
        finally { return chunk; }
    }

    public Chunk[][] getChunkField() { return chunkField; }

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
