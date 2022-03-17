package com.imaginegames.game.worlds;
import com.badlogic.gdx.utils.Null;
import com.imaginegames.game.utils.math.IntPair;

import java.io.Serializable;
import java.util.Arrays;

public class Chunk implements Serializable {
    static final long serialVersionUID = 237L;
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

}