package com.imaginegames.game.worlds;
import com.badlogic.gdx.utils.Null;
import com.imaginegames.game.utils.math.IntPair;

import java.util.Arrays;

public class Chunk {
    private static final byte size = 3;
    private IntPair xy;
    private int[][] cells;

    public Chunk(IntPair xy, int[][] cells) {
        this.xy = xy;
        if (cells != null) this.cells = cells;
        else this.cells = new int[size][size];
    }

    public IntPair getXY() { return xy; }
    public static int getSize() {
        return size;
    }
    public int[][] getCells() {
        return cells;
    }
    public void setCells(@Null int[][] cells) { this.cells = cells; }
}