package com.imaginegames.game.worlds;

public class World {
    private int width;
    private int height;
    private int[][] cells;

    private long seed;

    public World(long seed) {
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }

    public World(int[][] cells) {
        this.cells = cells;
        this.height = cells.length;
        this.width = cells[0].length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getCells() {
        return cells;
    }
}
