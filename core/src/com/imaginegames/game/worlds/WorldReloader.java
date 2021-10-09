package com.imaginegames.game.worlds.limited;

import com.imaginegames.game.utils.math.IntPair;

public class WorldReloader {
    private Chunk[][] field;
    private int fieldSize;
    private ChunkManager cm;
    private float oldx, oldy;

    public WorldReloader(Chunk[][] field, byte fieldSize, String worldFileName, float initialX, float initialY) {
        this.field = field;
        this.fieldSize = fieldSize;
        oldx = initialX;
        oldy = initialY;
        cm = new ChunkManager(worldFileName);

        for (int cy = 0; cy < fieldSize; cy++) {
            for (int cx = 0; cx < fieldSize; cx++) {
                field[cy][cx] = new Chunk(new IntPair(
                        30 + (int) oldx / Chunk.getSize() + cx,
                        30 - (int) oldy / Chunk.getSize() + cy), null);
            }
        }
        Chunk[] chunks = new Chunk[fieldSize * fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                chunks[i * fieldSize + j] = field[i][j];
            }
        }
        cm.load(chunks);
    }

    public void reload(float x, float y) {
        if ((int) x / Chunk.getSize() != (int) oldx / Chunk.getSize() || (int) y / Chunk.getSize() != (int) oldy / Chunk.getSize()) {
            for (int cy = 0; cy < fieldSize; cy++) {
                for (int cx = 0; cx < fieldSize; cx++) {
                    field[cy][cx] = new Chunk(new IntPair(
                            (int) x / Chunk.getSize() + cx,
                            (int) -y / Chunk.getSize() + cy), null);
                }
            }
            Chunk[] chunks = new Chunk[fieldSize * fieldSize];
            for (int i = 0; i < fieldSize; i++) {
                for (int j = 0; j < fieldSize; j++) {
                    chunks[i * fieldSize + j] = field[i][j];
                }
            }
            if (x / Chunk.getSize() != oldx / Chunk.getSize() || y / Chunk.getSize() != oldy / Chunk.getSize()) {
                cm.load(chunks);
                oldx = x;
                oldy = y;
            }
        }
    }
}
