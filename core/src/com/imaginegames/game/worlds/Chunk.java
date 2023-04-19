package com.imaginegames.game.worlds;
import com.badlogic.gdx.utils.Null;
import com.imaginegames.game.utils.math.IntPair;

import java.io.Serializable;
import java.util.Arrays;

public class Chunk implements Serializable {
    static final long serialVersionUID = 239L;
    private int[][] cells;

    public Chunk(int size) {
        this.cells = new int[size][size];
    }

    public Chunk(int[][] cells, byte size) {
        if (cells != null) this.cells = cells;
        else this.cells = new int[size][size];
    }

    public int[][] getCells() {
        return cells;
    }
    public void setCells(int[][] cells) { this.cells = cells; }
    public void setCell(int x, int y, int value) { cells[x][y] = value; }

    static public int getChunkCoord(float a, byte chunkSize) {
        a -= (a < 0 ? chunkSize : 0);
        return (int) a / chunkSize;
    }

    /**
     * "+ chunkSize" is used because Java uses negative remainders of the division - a thing
     * which in some situations leads to inconvenient and asymmetrical expressions like below one
     * */
    static public int getCellCoord(float a, byte chunkSize) {
        //int mod = a >= 0 ? (int) a % chunkSize : ((int) (a - 1) % chunkSize) + chunkSize;
        int mod = ((int) Math.floor((double) a) % chunkSize); // ??? ArrayOutOfIndex: 8 ?
        return mod < 0 ? mod + chunkSize : mod;
    }

}