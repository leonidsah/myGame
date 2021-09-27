package com.imaginegames.game.worlds;

public class Chunk extends WorldRegion {
    private static final int chunkWidth = 16, chunkHeight = 16;

    public Chunk(int x, int y) {
        super(x, y, chunkWidth, chunkHeight);
    }

    public int getChunkWidth() {
        return chunkWidth;
    }

    public int getChunkHeight() {
        return chunkHeight;
    }
}